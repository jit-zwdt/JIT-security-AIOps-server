package com.jit.zabbix.client.model.host;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Host Macro.
 *
 * @author Mamadou Lamine NIANG
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/usermacro/object#host_macro">Host macro</a>
 **/

public class HostMacro {

    @JsonProperty("hostmacroid")
    private String id;
    @JsonProperty("hostid")
    private String hostId;
    private String macro;
    private String value;

    public HostMacro() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
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
}
