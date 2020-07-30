package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.zabbix.client.model.problem.ProblemEvaltype;
import com.jit.zabbix.client.model.problem.ProblemObject;
import com.jit.zabbix.client.model.problem.ProblemSeverity;
import com.jit.zabbix.client.model.problem.ProblemSource;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetProblemParams extends CommonGetParams {

    @Singular
    private List<String> eventids;
    @Singular
    private List<String> groupids;
    @Singular
    private List<String> hostids;
    @Singular
    private List<String> objectids;
    @Singular
    private List<String> applicationids;
    private ProblemSource source;
    private ProblemObject object;
    private boolean acknowledged;
    private ProblemSeverity severity;
    private ProblemEvaltype evaltype;
}
