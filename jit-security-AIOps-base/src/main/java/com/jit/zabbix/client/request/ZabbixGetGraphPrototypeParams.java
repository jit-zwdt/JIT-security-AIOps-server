package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix method Graph Prototype.get parameters.
 *
 * @author lancelot
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/graphprototype/get#parameters">Method Graph Prototype.get parameters</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetGraphPrototypeParams extends CommonGetParams {

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

    @JsonProperty("graphids")
    @Singular
    private List<String> graphIds;

    @JsonProperty("discoveryids")
    @Singular
    private List<String> discoveryIds;

    private Boolean inherited;

    private Boolean templated;

    private Object selectDiscoveryRule;

    private Object selectGraphItems;
    private Object selectGroups;
    private Object selectHosts;
    private Object selectItems;
    private Object selectTemplates;
}
