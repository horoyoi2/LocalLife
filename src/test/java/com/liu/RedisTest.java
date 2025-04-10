package com.liu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void testRedis() {
        redisTemplate.opsForValue().set("testKey", "Spring Boot Redis OK!");
        String value = redisTemplate.opsForValue().get("testKey");
        System.out.println("Redis 返回值：" + value);
    }
}
