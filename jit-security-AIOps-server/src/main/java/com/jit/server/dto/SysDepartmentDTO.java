package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: SysDepartmentDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.19
 */
@Data
@AllArgsConstructor
public class SysDepartmentDTO {
    private String id;

    private String departName;

    private String description;

    private String departCategory;

    private String departType;
}
