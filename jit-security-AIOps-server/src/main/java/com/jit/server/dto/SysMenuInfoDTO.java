package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: SysMenuInfoDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenuInfoDTO {
    /**
     * 主键
     */
    private String id;

    /**
     * 菜单ID
     */
    private int sid;

    /**
     * 父菜单ID，一级菜单为0
     */
    private String parentId;

    /**
     * 组件名称
     */
    private String name;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 类型   0：菜单   1：按钮
     */
    private int menuType;

    /**
     * 权限策略 0: 显示；1：禁用
     */
    private int isShow;

    /**
     * 排序
     */
    private int orderNum;

    /**
     * 重定向
     */
    private String redirect;

    /**
     * 是否路由菜单: 0:不是  1:是（默认值1）
     */
    private int isRoute;

    /**
     * 是否隐藏路由: 0：否；1：是
     */
    private int hiddenRoute;

}
