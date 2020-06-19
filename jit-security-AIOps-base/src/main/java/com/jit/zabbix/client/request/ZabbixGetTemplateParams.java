package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

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
public class ZabbixGetTemplateParams extends CommonGetParams {

    @JsonProperty("templateids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> templateIds;

    @JsonProperty("groupids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> groupIds;

    @JsonProperty("parentTemplateids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> parentTemplateIds;

    @JsonProperty("hostids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> hostIds;

    @JsonProperty("graphids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> graphIds;

    @JsonProperty("itemids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> itemIds;

    @JsonProperty("triggerids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> triggerIds;

    @JsonProperty("with_items")
    private Boolean withItems;

    @JsonProperty("with_triggers")
    private Boolean withTriggers;

    @JsonProperty("with_graphs")
    private Boolean withGraphs;

    @JsonProperty("with_httptests")
    private Boolean withHttpTests;

    private Object selectGroups;

    private Object selectHosts;

    private Object selectTemplates;

    private Object selectParentTemplates;

    private Object selectHttpTests;

    private Object selectItems;

    private Object selectDiscoveries;

    private Object selectTriggers;

    private Object selectGraphs;

    private Object selectApplications;

    private Object selectMacros;

    private Object selectScreens;

    private int limitSelects;
}
