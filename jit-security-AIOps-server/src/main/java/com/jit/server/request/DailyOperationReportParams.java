package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/10/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailyOperationReportParams {

    /**
     * 运维人
     */
    private String operationUser;

    /**
     * 运维时间
     */
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    /**
     * 负责人签字
     */
    private String signature;

    /**
     * 负责人签字日期
     */
    @JsonDeserialize(using = CustomJsonSerializer.LocalDateStrDeserializer.class)
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
}
