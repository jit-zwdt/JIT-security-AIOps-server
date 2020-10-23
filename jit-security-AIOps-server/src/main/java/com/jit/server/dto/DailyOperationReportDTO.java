package com.jit.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @Description: DailyOperationReportDTO
 * @Author: zengxin_miao
 * @Date: 2020/10/21
 */
@Data
@NoArgsConstructor
public class DailyOperationReportDTO {
    private String id;
    private String operationUser;
    private LocalDate gmtCreate;
}
