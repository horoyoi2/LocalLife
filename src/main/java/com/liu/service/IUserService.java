package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.dto.LoginFormDTO;
import com.liu.dto.Result;
import com.liu.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);

    Result sign();

    Result signCount();

    Result logout(HttpServletRequest request);


}
