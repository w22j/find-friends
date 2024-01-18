package com.tu.hb.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 退出队伍请求体
 *
 * @author The tu
 */
@Data
public class TeamQuitRequest implements Serializable {
    /**
     * id
     */
    private Long teamId;

}
