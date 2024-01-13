package com.tu.hb.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author The tu
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 4747502829523257778L;

    private String userAccount;

    private String userPassword;


}
