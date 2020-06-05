package com.jit.zabbix.client.model.host;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;

/**
 * Zabbix Host group object.
 *
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostgroup/object#host_group">Host group</a>
 * @author Mamadou Lamine NIANG
 **/

public class ZabbixHostGroup {

    @JsonProperty("groupid")
    private String id;
    private String name;
    private OriginFlag flags;
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    private boolean internal;

    public ZabbixHostGroup() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OriginFlag getFlags() {
        return flags;
    }

    public void setFlags(OriginFlag flags) {
        this.flags = flags;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }
}
