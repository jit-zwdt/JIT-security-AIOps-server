package com.jit.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: CronExpressionDTO
 * @Author: zengxin_miao
 * @Date: 2020/10/10
 */
@Data
@NoArgsConstructor
public class CronExpressionDTO {
    private String cronExpressionDesc;
    private String cronExpression;
}
