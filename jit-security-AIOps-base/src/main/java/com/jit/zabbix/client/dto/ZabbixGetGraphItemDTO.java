package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.zabbix.client.model.graph.GraphItem;
import com.jit.zabbix.client.model.graph.GraphPrototype;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author lancelot
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/graphitem/get">method graphprototype.get</a>
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixGetGraphItemDTO extends GraphItem {

}
