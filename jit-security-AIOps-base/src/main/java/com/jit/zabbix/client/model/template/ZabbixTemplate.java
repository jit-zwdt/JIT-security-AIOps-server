package com.jit.zabbix.client.model.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Zabbix Template object.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/template/object#template">Template</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixTemplate {

    @JsonProperty("templateid")
    private String id;
    private String host;
    private String description;
    private String name;
}
