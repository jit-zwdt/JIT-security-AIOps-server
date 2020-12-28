package com.jit.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/12/28
 */

@Data
@NoArgsConstructor
public class MonitorRegisterEntityDTO {

    private String problemType;

    private String problemReason;


    private String problemSolution;

    private String problemProcess;

}
