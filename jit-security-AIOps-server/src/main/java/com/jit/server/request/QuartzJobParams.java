package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description: object role param
 * @Author: zengxin_miao
 * @Date: 2020.08.28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuartzJobParams {

    private String id;
    private String jobClassName;
    private String cronExpression;
    private String jobGroup;
    private String jsonParam;
    private String description;
    private int status;
}
