package com.tu.hb.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author The tu
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 309570602008263033L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;


}
