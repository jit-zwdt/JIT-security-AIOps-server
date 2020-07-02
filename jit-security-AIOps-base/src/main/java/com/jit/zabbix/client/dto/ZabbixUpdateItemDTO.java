package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.Data;
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
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixUpdateItemDTO {
    @JsonProperty("itemid")
    protected String id;
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    protected boolean status;
}
