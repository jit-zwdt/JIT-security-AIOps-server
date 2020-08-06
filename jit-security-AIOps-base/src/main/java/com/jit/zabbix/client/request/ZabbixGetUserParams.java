package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix method User.get parameters.
 *
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/User/get#parameters">Method User.get parameters</a>
 * @author jian_liu
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetUserParams extends CommonGetParams {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object selectMedias;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object selectMediatypes;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object selectUsrgrps;
}
