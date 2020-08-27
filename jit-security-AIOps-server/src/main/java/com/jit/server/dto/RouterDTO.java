package com.jit.server.dto;

import java.util.List;

/**
 * @Description: RouterDTO
 * @Author: zengxin_miao
 * @Date: 2020/08/27
 */
public class RouterDTO {
    private String path;
    private String component;
    private String redirect;
    private String name;
    private MetaDTO meta;
    List<RouterDTO> children;
}
