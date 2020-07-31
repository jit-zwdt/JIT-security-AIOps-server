package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.zabbix.client.model.graph.GraphPrototype;
import com.jit.zabbix.client.model.item.ZabbixItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author lancelot
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/graphprototype/get">method graphprototype.get</a>
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixGetGraphPrototypeDTO extends GraphPrototype {

}
