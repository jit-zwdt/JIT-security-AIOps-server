package com.jit.server.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.pojo.MonitorRegisterEntity;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemSolveReportDTO {
    private Integer index;
    private MonitorClaimEntity claim;
    private MonitorRegisterEntity register;
    private String user;
    private String role;
}
