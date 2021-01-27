package com.jit.server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorHostDetailBindItemDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 主机id
     */
    private String hostId;

    /**
     * 类型id
     */
    private String typeId;

    /**
     * 监控项id
     */
    private String itemId;

    /**
     * 监控项名称
     */
    private String itemName;

    /**
     * 要返回的历史对象类型
     */
    private int valueType;

    /**
     * 监控项单位
     */
    private String units;

    private LocalDateTime gmtCreate;
}
