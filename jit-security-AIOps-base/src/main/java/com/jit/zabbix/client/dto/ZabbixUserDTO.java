package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.zabbix.client.model.user.ZabbixUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Zabbix User DTO used as parameter in method user.object and returned in user.object.
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/user/object">method user.object</a>
 *
 * @author jian_liu
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixUserDTO extends ZabbixUser {

}
