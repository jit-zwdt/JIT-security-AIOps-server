package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: object role param
 * @Author: zengxin_miao
 * @Date: 2020.08.28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleParams {

    private String id;
    private String roleName;
    private String roleSign;
    private String remark;
}
