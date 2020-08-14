package com.jit.zabbix.client.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.user.ZabbixMediasUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZabbixUpdateMediaDTO {
    @JsonProperty("userid")
    private String userid;

    @JsonProperty("user_medias")
    private List<ZabbixMediasUpdate> list;
}
