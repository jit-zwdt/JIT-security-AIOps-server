package com.jit.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuDTO {
    private String id;
    private String path;
    private String component;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String redirect;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String permissionsKey;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SysMenuDTO> children;
    private SysMenuMetaDTO meta;
    private String isShow;
    private String isRoute;
}
