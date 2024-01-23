package com.tu.hb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tu.hb.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.tu.hb.constant.UserConstant.ADMIN_ROLE;
import static com.tu.hb.constant.UserConstant.USER_LOGIN_STATE;


/**
* @author The tu
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-12-26 15:41:38
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User setSafetyUser(User originUser);

    /**
     * 用户退出
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签名搜素用户
     * @param tagNameList 用户要拥有的标签
     * @return
     */
    List<User> searchUserByTags(List<String> tagNameList);

    /**
     * 更改用户信息
     * @param user
     * @return
     */
    int updateUser(User user, User loginUser);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 是否为管理员
     * @param request
     * @return
     */
    Boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param loginUser
     * @return
     */
    Boolean isAdmin(User loginUser);

    /**
     * 获取最匹配的用户
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);
}
