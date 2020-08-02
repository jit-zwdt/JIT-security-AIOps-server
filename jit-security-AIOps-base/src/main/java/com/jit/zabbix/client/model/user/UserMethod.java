package com.jit.zabbix.client.model.user;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 *
 * @author jian_liu
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/User/">item</a>
 **/
public enum UserMethod implements IZabbixMethod {

    CREATE("user.create"),
    DELETE("user.delete"),
    GET("user.get"),
    UPDATE("user.update");

    private String value;

    UserMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
