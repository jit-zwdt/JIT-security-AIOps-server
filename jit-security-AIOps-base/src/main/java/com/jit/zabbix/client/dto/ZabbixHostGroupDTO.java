package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.GlobalMacro;
import com.jit.zabbix.client.model.host.HostInventoryProperty;
import com.jit.zabbix.client.model.host.ZabbixHost;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;
import com.jit.zabbix.client.model.template.ZabbixTemplate;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * Zabbix Host Group DTO used as parameter in method hostgroup.create and returned in hostgroup.get.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostgroup/create">method hostgroup.create</a>
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixHostGroupDTO extends ZabbixHostGroup {

}
