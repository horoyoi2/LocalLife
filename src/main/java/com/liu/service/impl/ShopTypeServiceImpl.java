package com.liu.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liu.dto.Result;
import com.liu.entity.ShopType;
import com.liu.mapper.ShopTypeMapper;
import com.liu.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.liu.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;
import static com.liu.utils.RedisConstants.CACHE_SHOP_TYPE_TTL;


@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryTypeList() {
        // 1、从Redis中查询店铺类型
        String key = CACHE_SHOP_TYPE_KEY + UUID.randomUUID().toString();
        String shopTypeJSON = stringRedisTemplate.opsForValue().get(key);

        List<ShopType> typeList = null;
        // 2、判断缓存是否命中
        if (StrUtil.isNotBlank(shopTypeJSON)) {
            // 2.1 缓存命中，直接返回缓存数据
            typeList = JSONUtil.toList(shopTypeJSON, ShopType.class);
            return Result.ok(typeList);
        }
        // 2.1 缓存未命中，查询数据库
        typeList = this.list(new LambdaQueryWrapper<ShopType>()
                .orderByAsc(ShopType::getSort));

        // 3、判断数据库中是否存在该数据
        if (Objects.isNull(typeList)) {
            // 3.1 数据库中不存在该数据，返回失败信息
            return Result.fail("ストアタイプが存在しません");
        }
        // 3.2 店铺数据存在，写入Redis，并返回查询的数据
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(typeList),
                CACHE_SHOP_TYPE_TTL, TimeUnit.MINUTES);
        return Result.ok(typeList);
    }


}
