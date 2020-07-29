package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("hostids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> hostIds;

    private Object selectFunctions;
}
