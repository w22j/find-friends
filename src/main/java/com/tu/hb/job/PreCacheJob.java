package com.tu.hb.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tu.hb.common.ResultUtils;
import com.tu.hb.model.domain.User;
import com.tu.hb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
    private RedissonClient redissonClient;

    /**
     * 模拟重点用户
     */
    private List<Long> mainUsers = Arrays.asList(1L);

    @Scheduled(cron = "0 0 2 * * *")
    public void doRecommendUser() {
        RLock lock = redissonClient.getLock("hb:preCache:doCache:lock");
        try {
            // 只有一个线程能抢到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock:" + Thread.currentThread().getId());
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
        } catch (InterruptedException e) {
            log.error("doRecommendUser error", e);
        }finally {
            //只能释放自己的锁
            if (lock.isHeldByCurrentThread()){
                System.out.println("unlock:" + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
}
