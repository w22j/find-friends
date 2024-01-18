package com.tu.hb.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除队伍请求体
 *
 * @author The tu
 */
@Data
public class TeamDeleteRequest implements Serializable {
    /**
     * id
     */
    private Long teamId;

}
