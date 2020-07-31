package com.jit.zabbix.client.model.graph;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * Enum listing the different RPC methods available for Graph Item.
 *
 * @author lancelot
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/graph">graph</a>
 **/
public enum GraphItemMethod implements IZabbixMethod {

    GET("graphitem.get");

    private String value;

    GraphItemMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
