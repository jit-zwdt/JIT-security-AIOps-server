package com.jit.server.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemClaimDTO {
    private ZabbixProblemDTO zabbixProblemDTO;
    private int isClaim;
}
