package com.jit.server.pojo;

/**
 * @Description table sys_schedule_task: table entity
 * @author zengxin_miao
 * @Date: 2020/09/10 16:19:00
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sys_schedule_task")
public class SysScheduleTaskEntity {

    /**
     * 主键
     */
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 任务类名
     */
    @Column(name = "job_class_name")
    private String jobClassName;

    /**
     * 执行方法
     */
    @Column(name = "job_method_name")
    private String jobMethodName;

    /**
     * cron表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;

    /**
     * 参数
     */
    @Column(name = "json_param")
    private String jsonParam;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 分组
     */
    @Column(name = "job_group")
    private String jobGroup;

    /**
     * 状态（0：已启动；1：已停止）
     */
    @Column(name = "status")
    private int status;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime gmtModified;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 是否删除 1:表示删除;0:表示未删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;

}
