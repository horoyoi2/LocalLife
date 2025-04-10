package com.liu.controller;


import com.liu.dto.Result;
import com.liu.dto.UserDTO;
import com.liu.service.IFollowService;
import com.liu.service.impl.FollowServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/follow")
public class FollowController {
    @Resource
    private IFollowService followService;


    @PutMapping("/{id}/{isFollow}")
    public Result follow(@PathVariable("id") Long followUserId,@PathVariable("isFollow")Boolean isFollow){
        return followService.follow(followUserId,isFollow);
    }
    @GetMapping("/or/not/{id}")
    public Result follow(@PathVariable("id") Long followUserId){
        return followService.isFollow(followUserId);
    }
    @GetMapping("/common/{id}")
    public Result followcommons(@PathVariable("id") Long id){
        return followService.followCommons(id);
    }

}
