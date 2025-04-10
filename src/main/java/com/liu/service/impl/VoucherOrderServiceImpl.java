package com.liu.service.impl;

import com.liu.dto.Result;
import com.liu.entity.SeckillVoucher;
import com.liu.entity.VoucherOrder;
import com.liu.mapper.VoucherOrderMapper;
import com.liu.service.ISeckillVoucherService;
import com.liu.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.utils.RedisIdWorker;
import com.liu.utils.UserHolder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.time.LocalDateTime;



@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {
    @Resource
    private ISeckillVoucherService seckillVoucherService;
    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;


    @Override
    public Result seckillVoucher(Long voucherId) {
        //查询优惠券
        SeckillVoucher seckillVoucher = seckillVoucherService.getById(voucherId);
        //判断秒杀是否开始和结束
        if(seckillVoucher.getBeginTime().isAfter(LocalDateTime.now())){

            return Result.fail("フラッシュセールはまだ始まっていません");
        }
        if(seckillVoucher.getEndTime().isBefore(LocalDateTime.now())){

            return Result.fail("フラッシュセールは終了しました");
        }
        //判断库存
        if(seckillVoucher.getStock()<1){
            return Result.fail("在庫不足");
        }

        //一人一单
        Long userId = UserHolder.getUser().getId();
        // lock
        //SimpleRedisLock lock= new SimpleRedisLock("order:"+userId,stringRedisTemplate);
        RLock lock = redissonClient.getLock("order:"+userId);

        boolean islock = lock.tryLock();
        if(!islock){
            return Result.fail("1人1件のみ注文できます");
        }
        try {
            //获取代理对象
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        } finally {
            lock.unlock();
        }

    }


    @Transactional
    public  Result createVoucherOrder(Long voucherId) {
        Long userId = UserHolder.getUser().getId();

            //查询用户是否已经购买过该优惠券
            int count = Math.toIntExact(query().eq("user_id", userId).eq("voucher_id", voucherId).count());
            if (count > 0) {
                return Result.fail("このクーポンはすでに購入されています");
            }
            //扣减库存
            boolean success = seckillVoucherService.update()
                    .setSql("stock=stock-1")
                    .eq("voucher_id", voucherId).gt("stock", 0)
                    .update();
            if (!success) {
                return Result.fail("在庫不足");
            }
            //创建订单
            VoucherOrder voucherOrder = new VoucherOrder();
            //订单id
            long orderId = redisIdWorker.nextId("order");
            voucherOrder.setId(orderId);
            //用户id
            voucherOrder.setUserId(userId);
            //优惠券id
            voucherOrder.setVoucherId(voucherId);
            save(voucherOrder);

            //返回订单id
            return Result.ok(orderId);
        }


}
