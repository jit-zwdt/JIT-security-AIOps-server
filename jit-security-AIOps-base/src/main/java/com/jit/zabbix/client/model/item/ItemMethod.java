package com.jit.zabbix.client.model.item;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * Enum listing the different RPC methods available for Template.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/item/">item</a>
 **/
public enum ItemMethod implements IZabbixMethod {

    CREATE("item.create"),
    DELETE("item.delete"),
    GET("item.get"),
    UPDATE("item.update");

    private String value;

    ItemMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
