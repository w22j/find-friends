package com.tu.hb.model.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageQuery {

    /**
     * id
     */
    private Long id;

    /**
     * idList
     */
    private List<Long> idList;

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
     * 搜索关键词（同时对队伍名称和描述搜索）
     */
    private String searchText;

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
