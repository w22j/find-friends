package com.tu.hb.service;

import com.tu.hb.mapper.UserMapper;
import com.tu.hb.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
public class ImportUsersTest {

    @Resource
    private UserService userService;

    private ExecutorService executorService = new ThreadPoolExecutor(40, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));

    /**
     * 批量插入数据
     */
    @Test
    void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < INSERT_NUM; j++) {
                User user = new User();
                user.setUsername("假用户");
                user.setUserAccount("fakeUser");
                user.setUserPassword("12345678");
                user.setAvatarUrl("https://fastly.jsdelivr.net/npm/@vant/assets/ipad.jpeg");
                user.setGender(0);
                user.setPhone("12345678901");
                user.setEmail("1234@qq.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("11111");
                user.setTags("[]");
                userList.add(user);
            }

        }
        userService.saveBatch(userList, 1000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }


    /**
     * 并发插入数据
     */
    @Test
    void doConcurrentInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        int batchSize = 5000;
        int j = 0;
        //分十组
        for (int i = 0; i < 20; i++) {
            List<User> userList = new ArrayList<>();
            while (true) {
                j++;
                User user = new User();
                user.setUsername("假用户");
                user.setUserAccount("fakeUser");
                user.setUserPassword("12345678");
                user.setAvatarUrl("https://fastly.jsdelivr.net/npm/@vant/assets/ipad.jpeg");
                user.setGender(0);
                user.setPhone("12345678901");
                user.setEmail("1234@qq.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("11111");
                user.setTags("[]");
                userList.add(user);
                if (j % batchSize == 0) {
                    break;
                }
            }
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("treadName:" + Thread.currentThread().getName());
                userService.saveBatch(userList, batchSize);
            }, executorService);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
