package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description: SysDepartmentDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.19
 */
@Data
@AllArgsConstructor
public class SysDepartmentInfoDTO {
    /**
     * 主键
     */
    private String id;

    /**
     * 父机构ID
     */
    private String parentId;

    /**
     * 机构/部门名称
     */
    private String departName;

    /**
     * 英文名
     */
    private String departNameEn;

    /**
     * 缩写
     */
    private String departNameAbbr;

    /**
     * 排序
     */
    private int departOrder;

    /**
     * 描述
     */
    private String description;

    /**
     * 机构类别 1组织机构，2岗位
     */
    private String departCategory;

    /**
     * 机构类型 1一级部门 2子部门
     */
    private String departType;

    /**
     * 机构编码
     */
    private String departCode;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 传真
     */
    private String fax;

    /**
     * 地址
     */
    private String address;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（1启用，0不启用）
     */
    private int Status;
}
