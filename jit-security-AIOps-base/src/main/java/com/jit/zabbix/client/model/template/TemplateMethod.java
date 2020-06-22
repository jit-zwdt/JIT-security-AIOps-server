package com.jit.zabbix.client.model.template;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * Enum listing the different RPC methods available for Template.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/template/">template</a>
 **/
public enum TemplateMethod implements IZabbixMethod {

    CREATE("template.create"),
    DELETE("template.delete"),
    GET("template.get"),
    MASS_ADD("template.massadd"),
    MASS_REMOVE("template.massremove"),
    MASS_UPDATE("template.massupdate"),
    UPDATE("template.update");

    private String value;

    TemplateMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
