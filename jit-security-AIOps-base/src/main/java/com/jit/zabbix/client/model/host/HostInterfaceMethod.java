package com.jit.zabbix.client.model.host;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * Enum listing the different RPC methods available for Host.
 *
 * @author yongbin_jiang
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/hostinterface">hostinterface</a>
 **/
public enum HostInterfaceMethod implements IZabbixMethod {

    CREATE("hostinterface.create"),
    DELETE("hostinterface.delete"),
    GET("hostinterface.get"),
    MASS_ADD("hostinterface.massadd"),
    MASS_REMOVE("hostinterface.massremove"),
    MASS_UPDATE("hostinterface.massupdate"),
    UPDATE("hostinterface.update");

    private String value;

    HostInterfaceMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
