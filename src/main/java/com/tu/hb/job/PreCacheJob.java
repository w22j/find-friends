package com.tu.hb.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.hb.common.ResultUtils;
import com.tu.hb.model.domain.User;
import com.tu.hb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热任务
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 模拟重点用户
     */
    private List<Long> mainUsers = Arrays.asList(1L);

    @Scheduled(cron = "0 11 17 * * *")
    public void doRecommendUser() {
        //写入缓存并设置过期时间
        for (Long userId : mainUsers) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userList = userService.page(new Page<>(1, 20), queryWrapper);
            String redisKey = String.format("hb:user:recommend:%s", userId);
            ValueOperations valueOperations = redisTemplate.opsForValue();
            try {
                valueOperations.set(redisKey, userList, 30000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.error("redis set key error", e);
            }
        }
    }
}
