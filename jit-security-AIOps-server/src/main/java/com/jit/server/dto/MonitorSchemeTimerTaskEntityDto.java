package com.jit.server.dto;

import lombok.Data;

import java.util.Date;

/**
 * 显示巡检计划中心的 List 集合的 VO 对象增加了 status 状态值
 * @author oldwang <br />
 * 创建时间: 2020.10.21
 */
@Data
public class MonitorSchemeTimerTaskEntityDto {

    private String id;

    /**
     * 定时任务表ID
     */
    private String scheduleId;

    /**
     * ftp路径
     */
    private String ftpUrl;

    /**
     * 巡检计划名称
     */
    private String schemeName;

    /**
     * 创建时间
     */
    private java.time.LocalDateTime gmtCreate;

    /**
     * 父级 ID 如果没有父级 ID 默认为 1
     */
    private String parentId;

    /**
     * 状态（0：已启动；1：已停止）
     */
    private int status;
}
