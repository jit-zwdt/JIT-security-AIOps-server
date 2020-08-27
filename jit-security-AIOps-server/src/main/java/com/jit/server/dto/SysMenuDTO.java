package com.jit.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: SysMenuDto
 * @Author: zengxin_miao
 * @Date: 2020/08/27
 */
@Data
@NoArgsConstructor
public class SysMenuDTO {
    private String id;
    private String path;
    private String component;
    private String redirect;
    private String name;
    private String title;
    private String icon;
    private String isShow;
    public List<SysMenuDTO> children;
}
