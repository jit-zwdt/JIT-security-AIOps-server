package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: SysRoleDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysScheduleTaskDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 任务类名
     */
    private String jobClassName;

    /**
     * 执行方法
     */
    private String jobMethodName;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 参数
     */
    private String jsonParam;

    /**
     * 描述
     */
    private String description;

    /**
     * 分组
     */
    private String jobGroup;

    /**
     * 状态（0：已启动；1：已停止）
     */
    private int status;

}
