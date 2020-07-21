package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix method template.get parameters.
 *
 * @author jian_liu
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/trend/get">Method trend.get parameters</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetTrendParams extends CommonGetParams {

    @JsonProperty("time_from")
    @Singular
    private String time_from;

    @JsonProperty("time_till")
    @Singular
    private String time_till;

    @JsonProperty("itemids")
    @Singular
    private List<String> itemids;


}
