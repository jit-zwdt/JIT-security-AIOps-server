package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.zabbix.client.model.trigger.ZabbixTrigger;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Zabbix Item DTO used as parameter in method item.get.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/trigger/get">method item.get</a>
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixTriggerDTO extends ZabbixTrigger {

}
