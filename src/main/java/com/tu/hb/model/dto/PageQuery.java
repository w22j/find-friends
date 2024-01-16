package com.tu.hb.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = -5957699851975815091L;
    /**
     * 当前页数
     */
    private int pageNum = 1;

    /**
     * 每页条数
     */
    private int PageSize = 10;
}
