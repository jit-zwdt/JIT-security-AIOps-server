package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix method hostgroup.get parameters.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostgroup/get#parameters">Method hostgroup.get parameters</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetHostGroupParams extends CommonGetParams {

    @JsonProperty("graphids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> graphIds;

    @JsonProperty("groupids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> groupIds;

    @JsonProperty("hostids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> hostIds;

    @JsonProperty("maintenanceids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> maintenanceIds;

    @JsonProperty("monitored_hosts")
    private Boolean onlyMonitoredHosts;

    @JsonProperty("real_hosts")
    private Boolean realHosts;

    @JsonProperty("templated_hosts")
    private Boolean withTemplated;

    @JsonProperty("templateids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> templateIds;

    @JsonProperty("triggerids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> triggerIds;

    @JsonProperty("with_applications")
    private Boolean withApplications;

    @JsonProperty("with_graphs")
    private Boolean withGraphs;

    @JsonProperty("with_hosts_and_templates")
    private Boolean withHostsAndTemplates;

    @JsonProperty("with_httptests")
    private Boolean withHttpTests;

    @JsonProperty("with_items")
    private Boolean withItems;

    @JsonProperty("with_monitored_httptests")
    private Boolean withMonitoredHttpTests;

    @JsonProperty("with_monitored_items")
    private Boolean withMonitoredItems;

    @JsonProperty("with_monitored_triggers")
    private Boolean withMonitoredTriggers;

    @JsonProperty("with_simple_graph_items")
    private Boolean withSimpleGraphItems;

    @JsonProperty("with_triggers")
    private Boolean withTriggers;

    private Object selectDiscoveryRule;

    private Object selectGroupDiscovery;

    private Object selectHosts;

    private Object selectTemplates;

    private int limitSelects;

}
