package com.tu.hb.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserTagsUpdateRequest implements Serializable {

    private static final long serialVersionUID = -2389664874196290341L;
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户标签
     */
    private List<String> tagList;


}
