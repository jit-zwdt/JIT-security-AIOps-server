package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.GlobalMacro;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.model.template.ZabbixTemplate;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix Template DTO used as parameter in method template.create.
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
public class ZabbixCreateTemplateDTO extends ZabbixTemplate {

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
