package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix method hostinterface.get parameters.
 *
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/hostinterface/get#parameters">Method hostinterface.get parameters</a>
 * @author yongbin_jiang
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
// @Builder(builderMethodName = "paramsBuilder", buildMethodName = "buildParams")

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetHostInterfaceParams extends CommonGetParams {

    @JsonProperty("hostids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> hostIds;
    @JsonProperty("interfaceids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> interfaceIds;
}
