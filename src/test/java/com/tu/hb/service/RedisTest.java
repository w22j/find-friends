package com.tu.hb.service;
import java.util.Date;

import com.tu.hb.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void testRedis() {
        /*ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("wanStr", "wan");
        valueOperations.set("wanInt", 1);
        User user = new User();
        user.setId(1L);
        user.setUsername("wan");
        valueOperations.set("wanUser", user);
        Object wanStr = valueOperations.get("wanStr");
        Assertions.assertEquals("wan", wanStr);
        wanStr = valueOperations.get("wanInt");
        Assertions.assertEquals(1, wanStr);
        wanStr = valueOperations.get("wanUser");
        System.out.println(wanStr);*/
        redisTemplate.delete("wanStr");
        redisTemplate.delete("wanInt");
        redisTemplate.delete("wanUser");
    }
}
