package com.tu.hb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tu.hb.mapper.UserTeamMapper;
import com.tu.hb.model.domain.UserTeam;
import com.tu.hb.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
* @author The tu
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-01-16 11:06:43
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {

}




