package com.tu.hb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tu.hb.common.ErrorCode;
import com.tu.hb.constant.TeamStatus;
import com.tu.hb.exception.BusinessException;
import com.tu.hb.mapper.TeamMapper;
import com.tu.hb.model.domain.Team;
import com.tu.hb.model.domain.User;
import com.tu.hb.model.domain.UserTeam;
import com.tu.hb.service.TeamService;
import com.tu.hb.service.UserService;
import com.tu.hb.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * @author The tu
 * @description 针对表【team(队伍表)】的数据库操作Service实现
 * @createDate 2024-01-16 11:06:31
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private UserService userService;

    @Resource
    private UserTeamService userTeamService;

    @Override
    @Transactional
    public Long addTeam(Team team, User loginUser) {

        // 1. 请求参数是否为空
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 是否登录，未登录无法创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH, "用户未登录");
        }
        // 3. 校验信息
        //  ● 队伍标题不能为空且不能超过20个字
        if (StringUtils.isBlank(team.getName()) || team.getName().length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题不符合要求");
        }
        //  ● 队伍描述可以没有，有的话不能超过512
        if (StringUtils.isNotBlank(team.getDescription()) && team.getDescription().length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }
        //  ● 人数大于1，小于等于20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不符合要求");
        }
        //  ● status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatus statusEnum = TeamStatus.getEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //  ● 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatus.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
            }
        }
        //  ● 超时时间>当前时间
        if (team.getExpireTime().before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        //  ● 每个用户最多创建5个队伍
        final Long userId = loginUser.getId();
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long count = this.count(queryWrapper);
        if (count >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍创建已达上限");
        }
        // 4. 插入队伍信息到队伍表
        //先设置为空，让他自增
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
        }
        // 5. 插入用户、队伍到用户队伍关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
        }

        return teamId;
    }
}




