package com.jit.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/09/14
 */
@Data
@NoArgsConstructor
public class SchedulerDTO<T> {
    private String jobClassName;
    private String cronExpression;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String triggerDescription;
    private String jobDescription;
    private String jsonParam;
}
