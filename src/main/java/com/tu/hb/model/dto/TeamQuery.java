package com.tu.hb.model.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageQuery {

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


}
