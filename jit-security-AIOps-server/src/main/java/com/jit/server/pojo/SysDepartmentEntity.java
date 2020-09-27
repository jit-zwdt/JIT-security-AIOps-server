package com.jit.server.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zengxin_miao
 * @Description table sys_department: table entity
 * @Date: 2020/08/17 15:50:41
 */

@Entity
@Data
@Table(name = "sys_department")
public class SysDepartmentEntity {


    /**
     * 主键
     */
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 父机构ID
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 机构/部门名称
     */
    @Column(name = "depart_name")
    private String departName;

    /**
     * 英文名
     */
    @Column(name = "depart_name_en")
    private String departNameEn;

    /**
     * 缩写
     */
    @Column(name = "depart_name_abbr")
    private String departNameAbbr;

    /**
     * 排序
     */
    @Column(name = "depart_order")
    private int departOrder;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 机构类别 1组织机构，2岗位
     */
    @Column(name = "depart_category")
    private String departCategory;

    /**
     * 机构类型 1一级部门 2子部门
     */
    @Column(name = "depart_type")
    private String departType;

    /**
     * 机构编码
     */
    @Column(name = "depart_code")
    private String departCode;

    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 传真
     */
    @Column(name = "fax")
    private String fax;

    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（1启用，0不启用）
     */
    @Column(name = "status")
    private int status;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    @Column(name = "gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime gmtModified;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 是否删除 0：未删除 ；1：已删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;

}
