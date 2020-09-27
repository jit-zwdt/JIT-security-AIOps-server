package com.jit.server.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @Description table monitor_assets: table entity
 * @author zengxin_miao
 * @Date: 2020/09/01 09:32:38
 */
@Entity
@Data
@Table(name = "monitor_assets")
public class MonitorAssetsEntity {

    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 资产名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 资产编号
     */
    @Column(name = "number")
    private String number;

    /**
     * 资产分类 1：网络设备；2：通讯设备；3：服务器；4：云平台；
     */
    @Column(name = "type")
    private String type;

    /**
     * 使用状况 0：未用；1：在用；2：禁用；
     */
    @Column(name = "state")
    private String state;

    /**
     * 资产国标大类
     */
    @Column(name = "gb_type")
    private String gbType;

    /**
     * ip地址
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 备用ip
     */
    @Column(name = "backup_ip")
    private String backupIp;

    /**
     * 数量
     */
    @Column(name = "amount")
    private int amount;

    /**
     * 资产所属单位
     */
    @Column(name = "belongs_dept")
    private String belongsDept;

    /**
     * 资产所属人
     */
    @Column(name = "belongs_person")
    private String belongsPerson;

    /**
     * 资产登记时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "register_date")
    private LocalDateTime registerDate;

    /**
     * 登记人
     */
    @Column(name = "registrant")
    private String registrant;

    /**
     * 资产修改时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_date")
    private LocalDateTime  updateDate;

    /**
     * 资产位置
     */
    @Column(name = "location")
    private String location;

    /**
     * 资产注销时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "logout_date")
    private LocalDateTime  logoutDate;

    /**
     * 删除标识 0：表示未删除， 1：表示删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;

    /**
     * 财务入账日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "date_recorded")
    private LocalDateTime  dateRecorded;

    /**
     * 价值
     */
    @Column(name = "worth")
    private String worth;

    /**
     * 取得方式
     */
    @Column(name = "acquisition_mode")
    private String acquisitionMode;

    /**
     * 使用部门
     */
    @Column(name = "user_department")
    private String userDepartment;

    /**
     * 使用人
     */
    @Column(name = "user")
    private String user;

    /**
     * 用途分类
     */
    @Column(name = "object_classification")
    private String objectClassification;

    /**
     * 产品序列号
     */
    @Column(name = "sn")
    private String sn;

    /**
     * 品牌
     */
    @Column(name = "brand")
    private String brand;

    /**
     * 规格型号
     */
    @Column(name = "product_model")
    private String productModel;

    /**
     * 创建时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "gmt_create")
    private LocalDateTime  gmtCreate;

    /**
     * 修改时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "gmt_modified")
    private LocalDateTime  gmtModified;

}
