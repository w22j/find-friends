package com.tu.hb.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
            RLock lock = redissonClient.getLock("hb:preCache:doCache:lock");
            try {
                // 只有一个线程能抢到锁
                if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                    System.out.println("getLock:" + Thread.currentThread().getId());
                    //要执行的方法
                    Thread.sleep(300000);
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
