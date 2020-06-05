package com.jit.zabbix.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Zabbix Global macro.
 *
 * @author Mamadou Lamine NIANG
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/usermacro/object#global_macro">Global macro</a>
 **/

public class GlobalMacro {

    @JsonProperty("globalmacroid")
    private String id;
    private String macro;
    private String value;

    public GlobalMacro() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMacro() {
        return macro;
    }

    public void setMacro(String macro) {
        this.macro = macro;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GlobalMacro(id=" + getId() + ", macro=" + getMacro() + ", value=" + getValue() + ")";
    }

}
