package com.tu.hb.model.domain;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 队伍表
 * @TableName team
 */
@Data
public class Team implements Serializable {
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
     * 创建人(队长id)
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
    private Date expireTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}