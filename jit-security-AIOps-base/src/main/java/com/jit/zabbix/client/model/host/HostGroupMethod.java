package com.jit.zabbix.client.model.host;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * Enum listing the different RPC methods available for HostGroup.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostgroup">host group </a>
 **/
public enum HostGroupMethod implements IZabbixMethod {

    CREATE("hostgroup.create"),
    DELETE("hostgroup.delete"),
    GET("hostgroup.get"),
    MASS_ADD("hostgroup.massadd"),
    MASS_REMOVE("hostgroup.massremove"),
    MASS_UPDATE("hostgroup.massupdate"),
    UPDATE("hostgroup.update");

    private String value;

    HostGroupMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
