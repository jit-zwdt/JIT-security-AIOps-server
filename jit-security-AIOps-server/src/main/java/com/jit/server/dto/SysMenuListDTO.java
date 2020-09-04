package com.jit.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: SysMenuDto
 * @Author: jian_liu
 * @Date: 2020/09/01
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuListDTO {
    private String id;
    private String parentId;
    private String path;
    private String component;
    private String name;
    private String redirect;
    private String permissionsKey;
    private List<SysMenuListDTO> children;
    private String title;
    private String icon;
    private String isShow;
    private String isRoute;
    private String orderNum;
}
