package com.tu.hb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tu.hb.model.domain.Team;
import com.tu.hb.model.domain.User;
import com.tu.hb.model.domain.UserTeam;
import com.tu.hb.model.dto.TeamQuery;
import com.tu.hb.model.request.TeamDeleteRequest;
import com.tu.hb.model.request.TeamJoinRequest;
import com.tu.hb.model.request.TeamQuitRequest;
import com.tu.hb.model.request.TeamUpdateRequest;
import com.tu.hb.model.vo.TeamUserVO;

import java.util.List;

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

    /**
     * 查询队伍
     * @param teamQuery
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, User loginUser);

    /**
     * 修改队伍
     * @param teamUpdateRequest
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 队长删除队伍
     * @param teamDeleteRequest
     * @return
     */
    boolean deleteTeam(TeamDeleteRequest teamDeleteRequest, User loginUser);
}
