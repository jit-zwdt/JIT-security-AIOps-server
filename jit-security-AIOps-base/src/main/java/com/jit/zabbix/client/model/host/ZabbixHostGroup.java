package com.jit.zabbix.client.model.host;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.jit.zabbix.client.utils.CustomJsonSerializer.BooleanNumericDeserializer;
import static com.jit.zabbix.client.utils.CustomJsonSerializer.BooleanNumericSerializer;

/**
 * Zabbix Host group object.
 *
 * @author Mamadou Lamine NIANG
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostgroup/object#host_group">Host group</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixHostGroup {

    @JsonProperty("groupid")
    private String id;
    private String name;
    private OriginFlag flags;
    @JsonSerialize(using = BooleanNumericSerializer.class)
    @JsonDeserialize(using = BooleanNumericDeserializer.class)
    private boolean internal;
}
