package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Zabbix method host.get parameters.
 *
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/history/get">Method history.get parameters</a>
 * @author Mamadou Lamine NIANG
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetHistoryParams extends CommonGetParams {
    private int history;
    @JsonProperty("hostids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> hostIds;
    @JsonProperty("itemids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> itemIds;
    @JsonProperty("time_from")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime timeFrom;
    @JsonProperty("time_till")
    @JsonDeserialize(using= CustomJsonSerializer.DateStrDeserializer.class)
    protected LocalDateTime timeTill;
}
