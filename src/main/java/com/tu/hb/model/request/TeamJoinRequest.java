package com.tu.hb.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 加入队伍请求体
 *
 * @author The tu
 */
@Data
public class TeamJoinRequest implements Serializable {
    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 队伍密码
     */
    private String password;


}
