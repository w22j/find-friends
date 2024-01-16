package com.tu.hb.constant;

import io.swagger.models.auth.In;

public enum TeamStatus {
    PUBLIC("公开", 0),
    PRIVATE("私有", 1),
    SECRET("加密", 2);

    private String name;
    private Integer value;


    TeamStatus(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static TeamStatus getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (TeamStatus teamStatus : TeamStatus.values()) {
            if (teamStatus.getValue().equals(value)) {
                return teamStatus;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

}
