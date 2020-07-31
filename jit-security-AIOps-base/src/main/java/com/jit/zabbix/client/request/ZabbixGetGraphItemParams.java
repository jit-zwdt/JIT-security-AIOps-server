package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix method Graph Item.get parameters.
 *
 * @author lancelot
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/graphitem/get">Method Graph Prototype.get parameters</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetGraphItemParams extends CommonGetParams {

    @JsonProperty("gitemids")
    @Singular
    private List<String> gItemIds;

    @JsonProperty("graphids")
    @Singular
    private List<String> graphIds;

    @JsonProperty("itemids")
    @Singular
    private List<String> itemIds;

    private Integer type;
    private Object selectGraphs;
}
