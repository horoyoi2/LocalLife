package com.liu.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static cn.hutool.core.date.DateUtil.now;

@Component
public class RedisIdWorker {
    private static final long BEGIN_TIMESTAMP = 1707523200L;
    private static final int COUNT_BITS = 32;

    private StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    public long nextId(String keyPrefix) {
        //生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        //生成序列号
        long sequence = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + now().format(String.valueOf(DateTimeFormatter.ofPattern("yyyy:MM:dd"))));

        //返回数据
        return timestamp << COUNT_BITS | sequence;
    }

}
