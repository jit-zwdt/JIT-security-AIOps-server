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
     * 修改时间
     */
    private java.time.LocalDateTime gmtModified;

    /**
     * 父级 ID 如果没有父级 ID 默认为 1
     */
    private String parentId;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 是否删除 1:表示删除;0:表示未删除
     */
    private long isDeleted;

    /**
     * 状态（0：已启动；1：已停止）
     */
    private int status;
}
