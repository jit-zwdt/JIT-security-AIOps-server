package com.jit.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: MonitorDailyOperationReportDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorDailyOperationReportDTO {

    private String id;

    /**
     * 运维人
     */
    private String operationUser;

    /**
     * 运维时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    /**
     * 负责人签字
     */
    private String signature;

    /**
     * 负责人签字日期
     */
    private LocalDate signatureDate;

    /**
     * 出现问题-新增数
     */
    private String newProblemNum;

    /**
     * 出现问题-详细信息
     */
    private String newProblemDetail;

    /**
     * 出现问题-本月总数
     */
    private String newProblemTotal;

    /**
     * 认领问题-新增数
     */
    private String claimedProblemNum;

    /**
     * 认领问题-详细信息
     */
    private String claimedProblemDetail;

    /**
     * 认领问题-本月总数
     */
    private String claimedProblemTotal;

    /**
     * 处理中问题-新增数
     */
    private String processingProblemNum;

    /**
     * 处理中问题-详细信息
     */
    private String processingProblemDetail;

    /**
     * 处理中问题-本月总数
     */
    private String processingProblemTotal;

    /**
     * 解决问题-新增数
     */
    private String solvedProblemNum;

    /**
     * 解决问题-详细信息
     */
    private String solvedProblemDetail;

    /**
     * 解决问题-本月总数
     */
    private String solvedProblemTotail;

    private LocalDateTime gmtCreate;

}
