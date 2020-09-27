package com.jit.server.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: table sys_user entity
 * @Author: zengxin_miao
 * @Date: 2020/06/09 11:01
 */
@Data
@Entity
@Table(name = "sys_user")
public class SysUserEntity {

    /**
     * 主键
     */
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 登录账号
     */
    @Column(name = "username")
    private String username;

    /**
     * 真实姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 部门id
     */
    @Column(name = "department_id")
    private String departmentId;

    /**
     * 电子邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 状态 0:禁用，1:正常
     */
    @Column(name = "status")
    private int status;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime gmtModified;

    /**
     * 性别
     */
    @Column(name = "sex")
    private int sex;

    /**
     * 出身日期
     */
    @Column(name = "birth")
    private LocalDate birth;

    /**
     * 头像
     */
    @Column(name = "pic_id")
    private String picId;

    /**
     * 头像
     */
    @Column(name = "pic_url")
    private String picUrl;

    /**
     * 现居住地
     */
    @Column(name = "live_address")
    private String liveAddress;

    /**
     * 爱好
     */
    @Column(name = "hobby")
    private String hobby;

    /**
     * 省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 所在城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 是否允许登录zabbix系统 0：不允许；1：允许
     */
    @Column(name = "is_zabbix_active")
    private int isZabbixActive;

    /**
     * zabbix用户名
     */
    @Column(name = "zabbix_username")
    private String zabbixUsername;

    /**
     * zabbix用户密码
     */
    @Column(name = "zabbix_password")
    private String zabbixPassword;

    /**
     * 工号
     */
    @Column(name = "work_no")
    private String workNo;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 是否删除 0：未删除；1：已删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;
}
