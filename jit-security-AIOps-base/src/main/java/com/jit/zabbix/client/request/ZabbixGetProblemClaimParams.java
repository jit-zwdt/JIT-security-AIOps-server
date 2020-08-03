package com.jit.zabbix.client.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.zabbix.client.model.problem.ProblemSeverity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetProblemClaimParams extends CommonGetParams {
    private List<Integer> severities;
}
