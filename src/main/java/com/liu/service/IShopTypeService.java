package com.liu.service;

import com.liu.dto.Result;
import com.liu.entity.ShopType;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IShopTypeService extends IService<ShopType> {
    public Result queryTypeList();

}
