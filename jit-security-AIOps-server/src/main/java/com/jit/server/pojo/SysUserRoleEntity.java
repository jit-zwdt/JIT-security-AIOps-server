package com.jit.server.pojo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Description: table sys_user_role entity
 * @Author: zengxin_miao
 * @Date: 2020/06/09 11:01
 */
@Data
@Entity
@Table(name = "sys_user_role")
public class SysUserRoleEntity {
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "role_id", nullable = false)
    private String roleId;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "gmt_create")
    private Timestamp gmtCreate;

    @Column(name = "gmt_modified")
    private Timestamp gmtModified;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "is_deleted")
    private int isDeleted;
}
