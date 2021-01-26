package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: MonitorTypesDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorTypesDTO {

    private String id;

    /**
     * 类型
     */
    private String type;

    /**
     * 父id
     */
    private String pid;

    /**
     * 排序
     */
    private int orderNum;

}
