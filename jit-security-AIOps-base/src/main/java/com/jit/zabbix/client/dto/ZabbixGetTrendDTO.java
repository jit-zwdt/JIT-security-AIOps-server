package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.zabbix.client.model.item.ZabbixItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Zabbix Item DTO used as parameter in method item.get.
 *
 * @author jian_liu
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/trend/get">method trend.get</a>
 **/
@Data
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixGetTrendDTO {
    private String itemid;
    private String clock;
    private String num;
    private String value_min;
    private String value_avg;
    private String value_max;
}
