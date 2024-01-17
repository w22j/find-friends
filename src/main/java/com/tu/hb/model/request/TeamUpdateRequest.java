package com.tu.hb.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录请求体
 *
 * @author The tu
 */
@Data
public class TeamUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 队伍名
     */
    private String name;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 队伍状态 0-公开 1-私有 2-加密
     */
    private Integer status;

    /**
     * 队伍密码
     */
    private String password;

    /**
     * 过期时间
     */
    private Date expireTime;

}
