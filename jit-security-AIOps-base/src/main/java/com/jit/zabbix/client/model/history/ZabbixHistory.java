package com.jit.zabbix.client.model.history;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Zabbix Trigger object.
 *
 * @author yongbin_jiang
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/history/object">history</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixHistory {
    protected String id;
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime clock;
    @JsonProperty("itemid")
    protected String itemId;
    @JsonProperty("logeventid")
    protected int logeventId;
    protected int ns;
    protected int severity;
    protected String source;
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime timestamp;
    protected String value;
}
