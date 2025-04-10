package com.liu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.dto.Result;
import com.liu.dto.UserDTO;
import com.liu.entity.Follow;
import com.liu.mapper.FollowMapper;
import com.liu.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.service.IUserService;
import com.liu.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IUserService userService;

    @Override
    public Result follow(Long followUserId, boolean isFollow) {
        Long userId= UserHolder.getUser().getId();
        String key= "follow_" + userId;
        //判断关注还是取关
        if (isFollow) {
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            boolean isSucces= save(follow);
            if (isSucces) {

                stringRedisTemplate.opsForSet().add(key,followUserId.toString());
            }
        }else {
            boolean isSucces= remove(new QueryWrapper<Follow>().eq("user_id", userId).eq("follow_user_id", followUserId));
            if (isSucces) {
                stringRedisTemplate.opsForSet().remove(key, followUserId.toString());
            }

        }
        return Result.ok();
    }

    @Override
    public Result isFollow(Long followUserId) {
        Long userId=UserHolder.getUser().getId();
        //查询是否关注
        Long count = query().eq("user_id", userId).eq("follow_user_id", followUserId).count();
        return Result.ok(count);
    }

    @Override
    public Result followCommons(Long id) {
        Long userId=UserHolder.getUser().getId();
        //求交集(共同关注)
        String key1= "follows:" + userId;
        String key2= "follows:" + id;
        Set<String> intersect= stringRedisTemplate.opsForSet().intersect(key1,key2);
        if (intersect==null||intersect.isEmpty()) {
            return Result.ok(Collections.emptyList());
        }
        //解析id集合
        List<Long> ids= intersect.stream().map(Long::valueOf).collect(Collectors.toList());
        List<UserDTO> users=userService.listByIds(ids).stream().map(user -> BeanUtil.copyProperties(user,UserDTO.class)).collect(Collectors.toList());
        return Result.ok(users);
    }
}
