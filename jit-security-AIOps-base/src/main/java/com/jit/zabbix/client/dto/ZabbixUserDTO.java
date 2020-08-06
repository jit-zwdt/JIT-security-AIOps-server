package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.user.ZabbixMedias;
import com.jit.zabbix.client.model.user.ZabbixMediatypes;
import com.jit.zabbix.client.model.user.ZabbixUser;
import com.jit.zabbix.client.model.user.ZabbixUsrgrps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    @JsonProperty("medias")
    protected List<ZabbixMedias> zabbixMedias;

    @JsonProperty("mediatypes")
    protected List<ZabbixMediatypes> zabbixMediatypes;

    @JsonProperty("usrgrps")
    protected List<ZabbixUsrgrps> zabbixUsrgrps;

}
