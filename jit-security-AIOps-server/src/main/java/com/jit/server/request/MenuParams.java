package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.server.dto.SysMenuListDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: object menu param
 * @Author: jian_liu
 * @Date: 2020/09/01 13:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuParams {
    private String id;
    private String path;
    private String component;
    private String name;
    private String redirect;
    private String permissionsKey;
    private String title;
    private String icon;
    private String isShow;
    private String isRoute;
    private String orderNum;
    private String status;
    private String pid;
}
