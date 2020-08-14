package com.jit.zabbix.client.model.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixMediasUpdate {
    protected String mediatypeid;

    protected Object sendto;

    protected String active;

    protected String severity;

    protected String period;
}
