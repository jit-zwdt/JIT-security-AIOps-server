package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix method template.get parameters.
 *
 * @author Mamadou Lamine NIANG
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/template/get#parameters">Method template.get parameters</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetItemParams extends CommonGetParams {

    @JsonProperty("itemids")
    @Singular
    private List<String> itemIds;

    @JsonProperty("groupids")
    @Singular
    private List<String> groupIds;

    @JsonProperty("templateids")
    @Singular
    private List<String> templateIds;

    @JsonProperty("hostids")
    @Singular
    private List<String> hostIds;

    @JsonProperty("proxyids")
    @Singular
    private List<String> proxyIds;

    @JsonProperty("interfaceids")
    @Singular
    private List<String> interfaceIds;

    @JsonProperty("graphids")
    @Singular
    private List<String> graphIds;

    @JsonProperty("triggerids")
    @Singular
    private List<String> triggerIds;


    @JsonProperty("applicationids")
    @Singular
    private List<String> applicationIds;

    @JsonProperty("webitems")
    private Boolean webitems;

    private Boolean inherited;

    private Boolean templated;

    private Boolean monitored;

    private String group;

    private String host;

    private String application;

    @JsonProperty("with_triggers")
    private Boolean withTriggers;

    private Object selectHosts;

    private Object selectInterfaces;
}
