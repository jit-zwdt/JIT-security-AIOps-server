package com.jit.zabbix.client.model.trigger;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * Enum listing the different RPC methods available for Trigger.
 *
 * @author yongbin_jiang
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/trigger/">trigger</a>
 **/
public enum TriggerMethod implements IZabbixMethod {

    ADDDEPENDENCIES("trigger.adddependencies"),
    CREATE("trigger.create"),
    DELETE("trigger.delete"),
    DELETEDEPENDENCIES("trigger.deletedependencies"),
    GET("trigger.get"),
    UPDATE("trigger.update");

    private String value;

    TriggerMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
