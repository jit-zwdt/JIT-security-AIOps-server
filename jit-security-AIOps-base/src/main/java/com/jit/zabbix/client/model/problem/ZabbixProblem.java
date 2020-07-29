package com.jit.zabbix.client.model.problem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixProblem {

    @JsonProperty("eventid")
    protected String id;
    protected ProblemSource source;
    protected ProblemObject object;
    @JsonProperty("objectid")
    protected String objectId;
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime clock;
    protected Integer ns;
    @JsonProperty("r_eventid")
    protected String rEventid;
    @JsonProperty("r_clock")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime rClock;
    @JsonProperty("r_ns")
    protected Integer rNs;
    @JsonProperty("correlationid")
    protected String correlationId;
    @JsonProperty("userid")
    protected String userId;
    protected String name;
    @JsonSerialize(using = CustomJsonSerializer.BooleanNumericSerializer.class)
    @JsonDeserialize(using = CustomJsonSerializer.BooleanNumericDeserializer.class)
    protected boolean acknowledged;
    protected ProblemSeverity severity;
}
