package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.GlobalMacro;
import com.jit.zabbix.client.model.host.HostInventoryProperty;
import com.jit.zabbix.client.model.host.ZabbixHost;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;
import com.jit.zabbix.client.model.template.Template;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * Zabbix Host DTO used as parameter in method host.create and returned in host.get.
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/create">method host.create</a>
 *
 * @author Mamadou Lamine NIANG
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixHostDTO extends ZabbixHost {

    @Singular
    private List<ZabbixHostGroup> groups;
    @Singular
    private List<ZabbixHostInterface> interfaces;
    @JsonProperty("templates")
    @JsonAlias("parentTemplates")
    @Singular
    private List<Template> templates;
    @Singular
    private List<GlobalMacro> macros;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("inventory")
    private Map<HostInventoryProperty, String> inventory;
    @JsonProperty("templates_clear")
    @Singular("templateToClear")
    private List<Template> templatesToClear;
}
