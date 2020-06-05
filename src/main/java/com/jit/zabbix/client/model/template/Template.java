package com.jit.zabbix.client.model.template;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Zabbix template object.
 *
 * @author Mamadou Lamine NIANG
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/template/object#template">Template</a>
 **/
public class Template {

    @JsonProperty("templateid")
    private String id;
    private String host;
    private String description;
    private String name;

    public Template() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Template(id=" + getId() + ", host=" + getHost() + ", description=" + getDescription() + ", name=" + getName() + ")";
    }

}
