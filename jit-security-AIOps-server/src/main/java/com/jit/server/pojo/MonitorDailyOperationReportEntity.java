package com.jit.server.pojo;

/**
 * @Description table monitor_daily_operation_report: table entity
 * @author zengxin_miao
 * @Date: 2020/10/19 16:38:11
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "monitor_daily_operation_report")
public class MonitorDailyOperationReportEntity {

    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 运维人
     */
    @Column(name = "operation_user")
    private String operationUser;

    /**
     * 运维时间
     */
    @Column(name = "operation_time")
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    /**
     * 负责人签字
     */
    @Column(name = "signature")
    private String signature;

    /**
     * 负责人签字日期
     */
    @Column(name = "signature_date")
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateStrDeserializer.class)
    private LocalDate signatureDate;

    /**
     * 删除标识 0：表示未删除， 1：表示删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime gmtModified;

    /**
     * 出现问题-新增数
     */
    @Column(name = "new_problem_num")
    private String newProblemNum;

    /**
     * 出现问题-详细信息
     */
    @Column(name = "new_problem_detail")
    private String newProblemDetail;

    /**
     * 出现问题-本月总数
     */
    @Column(name = "new_problem_total")
    private String newProblemTotal;

    /**
     * 认领问题-新增数
     */
    @Column(name = "claimed_problem_num")
    private String claimedProblemNum;

    /**
     * 认领问题-详细信息
     */
    @Column(name = "claimed_problem_detail")
    private String claimedProblemDetail;

    /**
     * 认领问题-本月总数
     */
    @Column(name = "claimed_problem_total")
    private String claimedProblemTotal;

    /**
     * 处理中问题-新增数
     */
    @Column(name = "processing_problem_num")
    private String processingProblemNum;

    /**
     * 处理中问题-详细信息
     */
    @Column(name = "processing_problem_detail")
    private String processingProblemDetail;

    /**
     * 处理中问题-本月总数
     */
    @Column(name = "processing_problem_total")
    private String processingProblemTotal;

    /**
     * 解决问题-新增数
     */
    @Column(name = "solved_problem_num")
    private String solvedProblemNum;

    /**
     * 解决问题-详细信息
     */
    @Column(name = "solved_problem_detail")
    private String solvedProblemDetail;

    /**
     * 解决问题-本月总数
     */
    @Column(name = "solved_problem_totail")
    private String solvedProblemTotail;
}
