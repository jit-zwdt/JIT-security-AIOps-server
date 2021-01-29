package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: MonitorTopologyDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorTopologyDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 参数
     */
    private String jsonParam;

    /**
     * 名称
     */
    private String infoName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;


}
