package com.jit.server.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.server.pojo.MonitorClaimEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemSolveReportDTO {
    private Integer index;
    private MonitorClaimEntity claim;
    private MonitorRegisterEntityDTO register;
    private String user;
    private String role;
}
