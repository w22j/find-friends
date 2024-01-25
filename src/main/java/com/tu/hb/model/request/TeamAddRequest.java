package com.tu.hb.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建队伍请求体
 *
 * @author The tu
 */
@Data
public class TeamAddRequest implements Serializable {
    /**
     * 队伍名
     */
    private String name;

    /**
     * 队伍头像
     */
    private String avatarUrl;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 队伍最大人数
     */
    private Integer maxNum;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

}
