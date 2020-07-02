package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * Zabbix method host.get parameters.
 *
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/get#parameters">Method host.get parameters</a>
 * @author Mamadou Lamine NIANG
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetTriggerParams extends CommonGetParams {


    @JsonProperty("hostids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> hostIds;
}
