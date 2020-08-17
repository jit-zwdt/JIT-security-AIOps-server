package com.jit.server.pojo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Description: table sys_user_role entity
 * @Author: zengxin_miao
 * @Date: 2020/06/09 11:01
 */
@Data
@Entity
@Table(name = "sys_menu")
public class SysMenuEntity {

    /**
     * 主键
     */
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;


    /**
     * 父菜单ID，一级菜单为0
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 菜单名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 菜单URL
     */
    @Column(name = "url")
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @Column(name = "perms")
    private String perms;

    /**
     * 权限策略 0: 显示；1：禁用
     */
    @Column(name = "perms_type")
    private int permsType;

    /**
     * 组件
     */
    @Column(name = "component")
    private String component;

    /**
     * 组件名称
     */
    @Column(name = "component_name")
    private String componentName;

    /**
     * 类型   0：一级菜单   1：子菜单   2：按钮
     */
    @Column(name = "menu_type")
    private int menuType;

    /**
     * 菜单图标
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private int orderNum;

    /**
     * 是否路由菜单: 0:不是  1:是（默认值1）
     */
    @Column(name = "is_route")
    private int isRoute;

    /**
     * 是否隐藏路由: 0：否；1：是
     */
    @Column(name = "hidden_route")
    private int hiddenRoute;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private java.sql.Timestamp gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private java.sql.Timestamp gmtModified;

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
     * 是否删除 1:表示删除;0:表示未删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;

}
