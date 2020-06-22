package com.jit.server.pojo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Description: table sys_user entity
 * @Author: zengxin_miao
 * @Date: 2020/06/09 11:01
 */
@Data
@Entity
@Table(name = "sys_user")
public class SysUserEntity {
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "status")
    private Byte status;

    @Column(name = "user_id_create")
    private Long userIdCreate;

    @Column(name = "gmt_create")
    private Timestamp gmtCreate;

    @Column(name = "gmt_modified")
    private Timestamp gmtModified;

    @Column(name = "sex")
    private Long sex;

    @Column(name = "birth")
    private Timestamp birth;

    @Column(name = "live_address")
    private String liveAddress;

    @Column(name = "hobby")
    private String hobby;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "is_zabbix_active")
    private int isZabbixActive;

    @Column(name = "zabbix_username")
    private String zabbixUsername;

    @Column(name = "zabbix_password")
    private String zabbixPassword;

}
