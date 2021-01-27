package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: SysDictionaryDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDictionaryDTO {
    /**
     * 主键
     */
    private String id;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典编号
     */
    private String dictCode;

    /**
     * 描述
     */
    private String description;
}
