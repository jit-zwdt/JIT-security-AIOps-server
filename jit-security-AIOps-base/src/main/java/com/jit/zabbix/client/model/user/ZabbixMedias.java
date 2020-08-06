package com.jit.zabbix.client.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix User object.
 *
 * @author jian_liu
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/user/object#user">User object</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixMedias {

    protected String mediaid;

    protected String userid;

    protected String mediatypeid;

    protected Object sendto;

    protected String active;

    protected String severity;

    protected String period;
}
