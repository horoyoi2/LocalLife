package com.liu.service.impl;

import com.liu.entity.UserInfo;
import com.liu.mapper.UserInfoMapper;
import com.liu.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
