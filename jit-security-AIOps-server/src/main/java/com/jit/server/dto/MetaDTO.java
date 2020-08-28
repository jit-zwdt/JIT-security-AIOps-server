package com.jit.server.dto;

import lombok.Data;

/**
 * @Description: MetaDTO
 * @Author: zengxin_miao
 * @Date: 2020/08/27
 */
@Data
public class MetaDTO {
    private String title;
    private String icon;
    private boolean requireAuth;
}
