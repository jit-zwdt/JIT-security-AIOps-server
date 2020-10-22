package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix method history.get parameters.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/history/get">Method history.get parameters</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetHistoryParams extends CommonGetParams {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    private String timeFrom;
    @JsonProperty("time_till")
    private String timeTill;
}
