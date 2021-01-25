package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: SysCronExpressionDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysCronExpressionDTO {
    /**
     * 主键
     */
    private String id;

    /**
     * 表达式描述
     */
    private String cronExpressionDesc;

    /**
     * cron表达式
     */
    private String cronExpression;
    
}
