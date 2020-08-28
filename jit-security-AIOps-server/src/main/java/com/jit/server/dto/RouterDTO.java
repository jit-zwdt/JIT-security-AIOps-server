package com.jit.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @Description: RouterDTO
 * @Author: zengxin_miao
 * @Date: 2020/08/27
 */
@Data
public class RouterDTO {
    private String name;
    private String path;
    private String component;
    private String redirect;
    private String permissionsKey;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MetaDTO meta;
    List<RouterDTO> children;
}
