package com.tu.hb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tu.hb.model.domain.Team;
import com.tu.hb.model.domain.User;

/**
 * @author The tu
 * @description 针对表【team(队伍表)】的数据库操作Service
 * @createDate 2024-01-16 11:06:31
 */
public interface TeamService extends IService<Team> {

     /**
      * 创建队伍
      * @param team
      * @param loginUser
      * @return
      */
    Long addTeam(Team team, User loginUser);

}
