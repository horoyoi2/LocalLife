package com.liu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class ProjectApplicationTests {

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Test
	void contextLoads() {
		redisTemplate.opsForValue().set("testKey", "Spring Boot Redis OK!");
		String value = redisTemplate.opsForValue().get("testKey");
		System.out.println("Redis 返回值：" + value);
	}

}
