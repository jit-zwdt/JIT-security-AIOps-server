package com.jit.server.pojo;

/**
 * @Description table sys_quartz_job: table entity
 * @author zengxin_miao
 * @Date: 2020/09/10 16:19:00
 */

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "sys_quartz_job")
public class SysQuartzJobEntity {


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
     * 状态（0：正常；1：停止）
     */
    @Column(name = "status")
    private int status;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private java.sql.Timestamp gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private java.sql.Timestamp gmtModified;

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
