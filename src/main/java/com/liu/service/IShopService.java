package com.liu.service;

import com.liu.dto.Result;
import com.liu.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IShopService extends IService<Shop> {
    Result queryById(Long id) throws InterruptedException;
    Result update(Shop shop);

}
