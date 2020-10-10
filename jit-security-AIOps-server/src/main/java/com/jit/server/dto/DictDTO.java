package com.jit.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: DictDTO
 * @Author: zengxin_miao
 * @Date: 2020/10/10
 */
@Data
@NoArgsConstructor
public class DictDTO {
    private String text;
    private String value;
    private boolean status;
}
