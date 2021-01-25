package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: SysDictionaryItemDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDictionaryItemDTO {
    /**
     * 主键
     */
    private String id;

    /**
     * 字典ID
     */
    private String dictId;

    /**
     * 字段名称
     */
    private String itemText;

    /**
     * 字段值
     */
    private String itemValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态
     */
    private Integer status;
}
