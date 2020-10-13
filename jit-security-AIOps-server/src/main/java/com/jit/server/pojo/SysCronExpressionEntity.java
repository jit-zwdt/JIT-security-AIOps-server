package com.jit.server.pojo;

/**
 * @Description table sys_cron_expression: table entity
 * @author zengxin_miao
 * @Date: 2020/10/13 14:36:42
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_cron_expression")
public class SysCronExpressionEntity {


    /**
     * 主键
     */
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 表达式描述
     */
    @Column(name = "cron_expression_desc")
    private String cronExpressionDesc;

    /**
     * cron表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;

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
