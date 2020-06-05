package com.jit.zabbix.client.dto;

/**
 * Represents an Zabbix authentication DTO.
 *
 * @author Mamadou Lamine NIANG
 **/

public class ZabbixAuthDTO {

    private String user;

    private String password;

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ZabbixAuthDTO() {
    }

    public ZabbixAuthDTO(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }
}
