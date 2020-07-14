package com.jit.zabbix.client.model.history;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * Enum listing the different RPC methods available for Trigger.
 *
 * @author yongbin_jiang
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/history/">history</a>
 **/
public enum HistoryMethod implements IZabbixMethod {

    GET("history.get");

    private String value;

    HistoryMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
