package com.liu.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.liu.dto.Result;
import com.liu.entity.Shop;
import com.liu.mapper.ShopMapper;
import com.liu.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.liu.utils.RedisConstants.CACHE_SHOP_KEY;
import static com.liu.utils.RedisConstants.CACHE_SHOP_TTL;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private boolean tryLock (String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(flag);
    }
    private void unLock(String key){
        stringRedisTemplate.delete(key);
    }

    @Override
    public Result queryById(Long id) throws InterruptedException {
        //互斥锁
        Shop shop = queryByIdWithMutex(id);

        return Result.ok(shop);
    }
    public Shop queryByIdWithMutex(Long id) throws InterruptedException {

        String key = CACHE_SHOP_KEY + id;
        //从redis查询
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        //判断缓存是否命中
        if(StrUtil.isNotBlank(shopJson)){
            //缓存命中，直接返回店铺数据
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        //判断命中是否为空
        if(shopJson != null){
            return null;
        }
        //尝试获取互斥锁
        String lockKey = "lock:shop:" + id;
        boolean isLock = tryLock(lockKey);
        //获取锁失败，则休眠并重试
        if(!isLock){
            Thread.sleep(50);
            queryByIdWithMutex(id);
        }
        //缓存未命中，从数据库中查询店铺数据
        Shop shop = getById(id);
        //如果数据库中也不存在，返回错误
        if(shop == null){
            return null;
        }
        //存在,写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop),CACHE_SHOP_TTL, TimeUnit.MINUTES);
        //释放锁
        unLock(lockKey);
        return null;


    }

    @Override
    @Transactional
    public Result update(Shop shop) {
        if(shop.getId() == null){
            return Result.fail("ストアIDは空欄にできません");
        }
        //更新数据库
        updateById(shop);
        //删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + shop.getId());
        return Result.ok();
    }

    public Shop queryByIdWithPassThrough(Long id) {

        String key = CACHE_SHOP_KEY + id;
        //从redis查询
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        //判断缓存是否命中
        if(StrUtil.isNotBlank(shopJson)){
            //缓存命中，直接返回店铺数据
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        //判断命中是否为空
        if(shopJson != null){
            return null;
        }
        //缓存未命中，从数据库中查询店铺数据
        Shop shop = getById(id);
        //如果数据库中也不存在，返回错误
        if(shop == null){
            return null;
        }
        //存在,写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop),CACHE_SHOP_TTL, TimeUnit.MINUTES);

        return null;

    }



}
