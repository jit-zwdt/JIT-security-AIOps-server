package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.GlobalMacro;
import com.jit.zabbix.client.model.host.HostInventoryProperty;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;
import com.jit.zabbix.client.model.template.ZabbixTemplate;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * Zabbix Template DTO used as parameter in method template.create and returned in template.get.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/template/create">method template.create</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixTemplateDTO extends ZabbixTemplate {

    @Singular
    private List<ZabbixHostGroup> groups;
    @JsonProperty("templates")
    @JsonAlias("parentTemplates")
    @Singular
    private List<ZabbixTemplate> templates;
    @Singular
    private List<GlobalMacro> macros;
    @Singular
    private List<ZabbixHostDTO> hosts;
}
