package com.jit.server.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @Description: table sys_menu entity
 * @Author: zengxin_miao
 * @Date: 2020.08.27
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
     * 菜单ID
     */
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "sid" , nullable=false , insertable=false , updatable=false , columnDefinition="numeric(19,0) IDENTITY")
    private int sid;

    /**
     * 父菜单ID，一级菜单为0
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 组件名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 菜单名称
     */
    @Column(name = "title")
    private String title;

    /**
     * 菜单URL
     */
    @Column(name = "path")
    private String path;

    /**
     * 组件
     */
    @Column(name = "component")
    private String component;

    /**
     * 菜单图标
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 类型   0：菜单   1：按钮
     */
    @Column(name = "menu_type")
    private int menuType;

    /**
     * 权限策略 0: 显示；1：禁用
     */
    @Column(name = "is_show")
    private int isShow;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private int orderNum;

    /**
     * 重定向
     */
    @Column(name = "redirect")
    private String redirect;

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
