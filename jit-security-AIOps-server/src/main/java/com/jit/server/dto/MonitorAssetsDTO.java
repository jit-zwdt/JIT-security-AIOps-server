package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @Description: MonitorAssetsDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorAssetsDTO {

    private String id;

    /**
     * 资产名称
     */
    private String name;

    /**
     * 资产编号
     */
    private String number;

    /**
     * 资产类别 0：硬件；1：软件
     */
    private String type;

    /**
     * 使用状况 0：未用；1：在用；2：禁用；
     */
    private String state;

    /**
     * 资产分类
     */
    private String classify;

    /**
     * 资产国标大类
     */
    private String gbType;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 备用ip
     */
    private String backupIp;

    /**
     * 数量
     */
    private int amount;

    /**
     * 资产所属单位
     */
    private String belongsDept;

    /**
     * 资产所属人
     */
    private String belongsPerson;

    /**
     * 资产登记时间
     */
    private LocalDate registerDate;

    /**
     * 登记人
     */
    private String registrant;

    /**
     * 资产修改时间
     */
    private LocalDate updateDate;

    /**
     * 资产位置
     */
    private String location;

    /**
     * 资产注销时间
     */
    private LocalDate logoutDate;

    /**
     * 财务入账日期
     */
    private LocalDate dateRecorded;

    /**
     * 价值
     */
    private String worth;

    /**
     * 取得方式
     */
    private String acquisitionMode;

    /**
     * 使用部门
     */
    private String userDepartment;

    /**
     * 使用人
     */
    private String user;

    /**
     * 用途分类
     */
    private String objectClassification;

    /**
     * 产品序列号
     */
    private String sn;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 规格型号
     */
    private String productModel;

    /**
     * 父硬件ID
     */
    private String parentId;

    /**
     * CPU 大小
     */
    private Float cpu;

    /**
     * CPU 核心数
     */
    private Integer cpuCoreNumber;

    /**
     * 内存参数值 单位 G
     */
    private Integer memory;

    /**
     * 硬件参数值 单位 G
     */
    private Integer hardDisk;

}
