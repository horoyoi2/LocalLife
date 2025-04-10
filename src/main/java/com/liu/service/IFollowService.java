package com.liu.service;

import com.liu.dto.Result;
import com.liu.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IFollowService extends IService<Follow> {
    Result follow(Long followUserId,boolean isFollow);
    Result isFollow(Long followUserId);
    Result followCommons(Long id);

}
