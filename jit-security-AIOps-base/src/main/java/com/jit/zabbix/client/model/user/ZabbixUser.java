package com.jit.zabbix.client.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jit.zabbix.client.model.host.OriginFlag;
import com.jit.zabbix.client.model.item.*;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.jit.zabbix.client.utils.CustomJsonSerializer.BooleanNumericDeserializer;
import static com.jit.zabbix.client.utils.CustomJsonSerializer.BooleanNumericSerializer;

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
public class ZabbixUser {

    @JsonProperty("userid")
    protected String id;

    protected String alias;

    @JsonProperty("attempt_clock")
    @JsonDeserialize(using = CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime attemptClock;

    @JsonProperty("attempt_failed")
    protected int attemptFailed;

    @JsonProperty("attempt_ip")
    protected String attemptIp;

    protected int autologin;

    protected String autologout;

    protected String lang;

    protected String name;

    protected String refresh;

    @JsonProperty("rows_per_page")
    protected int rowsPerPage;

    protected String surname;

    protected String theme;

    protected int type;

    protected String url;
}
