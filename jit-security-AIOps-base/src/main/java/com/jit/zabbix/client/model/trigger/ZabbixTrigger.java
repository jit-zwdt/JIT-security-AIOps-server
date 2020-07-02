package com.jit.zabbix.client.model.trigger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Zabbix Trigger object.
 *
 * @author yongbin_jiang
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/trigger/object#trigger">Trigger</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixTrigger {

    @JsonProperty("triggerid")
    private String id;
    private String description;
    private String expression;
    private String comments;
    private int priority;
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    protected boolean status;
    private String templateid;
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    protected boolean type;
    private String url;
    @JsonProperty("recovery_mode")
    private int recoveryMode;
    @JsonProperty("recovery_expression")
    private String recoveryExpression;
    @JsonProperty("correlation_mode")
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    private boolean correlationMode;
    @JsonProperty("correlation_tag")
    private String correlationTag;
    @JsonProperty("manual_close")
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    private boolean manualClose;
}
