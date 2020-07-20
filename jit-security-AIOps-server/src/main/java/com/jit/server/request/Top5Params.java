package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: Top5Params
 * @Author: zengxin_miao
 * @Date: 2020.07.20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Top5Params {
    private String typeId;
    private String subTypeId;
    private String itemKey;
    private String method;
    private String valueType;
}
