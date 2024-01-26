package com.tu.hb.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tu.hb.model.domain.User;
import com.tu.hb.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SpringBootTest
class UserServiceTest extends UserServiceImpl {

    @Resource
    private UserService userService;

    @Test
    void userRegister() {
        String username = "wang";
        String userAccount = "wang";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        long result = userService.userRegister(username, userAccount, userPassword, checkPassword);
        /*Assertions.assertEquals(-1, result);
        userAccount = "yu";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yupi";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yu pi";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "dog";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yupi";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertTrue(result > 0);*/
    }

    @Test
    void testSearchUserByTags() {
        List<String> tagNameList = Arrays.asList("java", "python");
        List<User> userList = userService.searchUserByTags(tagNameList);
        Assert.assertNotNull(userList);
    }

    @Test
    void testUpdateUserByTags() {
        List<String> list = Arrays.asList("java", "大一", "男");
        String[] array = list.toArray(new String[0]);
        String tags = "[" + String.join(",", array) + "]";
        Gson gson = new Gson();
        List<String> oldTagsList = gson.fromJson(tags, new TypeToken<List<String>>() {}.getType());
        System.out.println(oldTagsList);
    }
}