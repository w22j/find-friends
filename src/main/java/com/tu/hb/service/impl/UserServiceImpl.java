package com.tu.hb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tu.hb.exception.BusinessException;
import com.tu.hb.common.ErrorCode;
import com.tu.hb.mapper.UserMapper;
import com.tu.hb.model.domain.User;
import com.tu.hb.service.UserService;
import com.tu.hb.utils.AlgorithmUtils;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.tu.hb.constant.UserConstant.ADMIN_ROLE;
import static com.tu.hb.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author The tu
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-12-26 15:41:38
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值、混淆密码
     */
    private static final String SALT = "yupi";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号过长");
        }
        // 账号不能包含特殊字符
        String regex = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }
        // 两次输入密码一致
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }
        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编号重复");
        }
        //加密
        String dealPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(dealPassword);
        user.setPlanetCode(planetCode);
        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 账号不能包含特殊字符
        String regex = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }
        // 加密
        String dealPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", dealPassword);
        User user = this.getOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount can not match userPassword!");
            throw new BusinessException(ErrorCode.NULL_ERROR, "查无用户");
        }
        // 信息脱敏
        User safetyUser = setSafetyUser(user);
        // 记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public User setSafetyUser(User originUser) {
        if (originUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setTags(originUser.getTags());
        safetyUser.setProfile(originUser.getProfile());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 根据标签搜索用户（内存过滤）
     * @param tagNameList 用户要拥有的标签
     * @return
     */
    @Override
    public List<User> searchUserByTags(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        /*for (User user : userList) {
            String tagStr = user.getTags();
            Set<String> tempTagNameSet = gson.fromJson(tagStr, new TypeToken<Set<String>>(){}.getType());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
        }
        return true;*/
        // 1.查询全部用户
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson() ;
        // 2.在内存中判断是否包含要求的标签
        return userList.stream().filter(user -> {
            String tagStr= user.getTags();
            Set<String> tempTagNameSet = gson.fromJson(tagStr, new TypeToken<Set<String>>() {
            }.getType());
            //判空，如果标签为空，取new HashSet<>()
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::setSafetyUser).collect(Collectors.toList());
    }

    @Override
    public int updateUser(User user, User loginUser) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = user.getId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //是管理员，直接更改
        //不是管理员，只有当前登录用户可更改
        if (!isAdmin(loginUser) && loginUser.getId() != userId) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return (User) userObj;
    }

    @Override
    public Boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    @Override
    public Boolean isAdmin(User loginUser) {
        return loginUser != null && loginUser.getUserRole() == ADMIN_ROLE;
    }

    @Override
    public List<User> matchUsers(long num, User loginUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tags");
        queryWrapper.isNotNull("tags");
        List<User> userList = this.list(queryWrapper);
        String tags = loginUser.getTags();
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
        // 用户列表的下标 => 相似度
        List<Pair<User, Long>> list = new ArrayList<>();
        for (User user : userList) {
            String userTags = user.getTags();
            if (loginUser.getId().equals(user.getId()) || StringUtils.isBlank(userTags)) {
                continue;
            }
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {
            }.getType());
            // 计算分数
            long distance = AlgorithmUtils.minDistance(tagList, userTagList);
            list.add(new Pair<>(user, distance));
        }
        // 按编辑距离从小到大排序
        List<Pair<User, Long>> topUserList = list.stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .limit(num)
                .collect(Collectors.toList());
        // 排好顺序的id列表
        List<Long> userIdList = topUserList.stream()
                .map(pair -> pair.getKey().getId())
                .collect(Collectors.toList());
        // in不会按照指定的顺序去查询，要存入map里面去映射一下
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIdList);
        // 1, 3, 2
        // User1、User2、User3
        // 1 => User1, 2 => User2, 3 => User3
        Map<Long, List<User>> userIdUserListMap = this.list(userQueryWrapper).stream()
                .map(this::setSafetyUser)
                .collect(Collectors.groupingBy(User::getId));
        //对排好顺序的列表进行遍历，并从map中取出对象存入最终的列表
        List<User> finalUserList = new ArrayList<>();
        for (Long userId : userIdList) {
            finalUserList.add(userIdUserListMap.get(userId).get(0));
        }
        return finalUserList;
    }

    /**
     * 根据标签搜索用户（SQL查询）
     * @param tagNameList 用户要拥有的标签
     * @return
     */
    @Deprecated
    public List<User> searchUserByTagsSQL(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 拼接and查询
        // like '%Java%' and like '%c++%'
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags", tagName);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().map(this::setSafetyUser).collect(Collectors.toList());
    }




}




