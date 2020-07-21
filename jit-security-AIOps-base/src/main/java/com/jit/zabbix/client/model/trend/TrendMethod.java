package com.jit.zabbix.client.model.trend;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * Enum listing the different RPC methods available for Template.
 *
 * @author jian_liu
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/trend/">trend</a>
 **/
public enum TrendMethod implements IZabbixMethod {

    GET("trend.get");

    private String value;

    TrendMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
