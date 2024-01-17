package com.tu.hb.constant;

public enum TeamStatusEnum {
    PUBLIC("公开", 0),
    PRIVATE("私有", 1),
    SECRET("加密", 2);

    private String name;
    private Integer value;


    TeamStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static TeamStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (TeamStatusEnum teamStatusEnum : TeamStatusEnum.values()) {
            if (teamStatusEnum.getValue().equals(value)) {
                return teamStatusEnum;
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
