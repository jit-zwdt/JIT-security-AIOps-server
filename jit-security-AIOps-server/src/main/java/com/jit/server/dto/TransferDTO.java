package com.jit.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TransferDTO
 * @Author: zengxin_miao
 * @Date: 2020/09/01
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferDTO {
    private String key;
    private String label;
    private Boolean disabled;
}
