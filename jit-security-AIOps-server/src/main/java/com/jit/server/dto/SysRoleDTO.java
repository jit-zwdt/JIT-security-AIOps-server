package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: SysRoleDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleDTO {
    /**
     * 主键
     */
    private String id;

    private String roleName;

    private String roleSign;

    private String remark;

}
