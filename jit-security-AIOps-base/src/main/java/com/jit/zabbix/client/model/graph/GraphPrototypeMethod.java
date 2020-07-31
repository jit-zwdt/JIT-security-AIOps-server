package com.jit.zabbix.client.model.graph;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * Enum listing the different RPC methods available for Graph Prototype.
 *
 * @author lancelot
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/graph">graph</a>
 **/
public enum GraphPrototypeMethod implements IZabbixMethod {

    CREATE("graph.create"),
    DELETE("graph.delete"),
    GET("graph.get"),
    UPDATE("graph.update");

    private String value;

    GraphPrototypeMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
