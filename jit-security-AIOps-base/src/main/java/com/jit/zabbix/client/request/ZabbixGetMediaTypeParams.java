package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Zabbix method mediatype.get parameters.
 *
 * @author zengxin_miao
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/mediatype/get#parameters">Method mediatype.get parameters</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZabbixGetMediaTypeParams extends CommonGetParams {

    @JsonProperty("mediatypeids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> mediatypeIds;

    @JsonProperty("mediaids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> mediaIds;

    @JsonProperty("userids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular
    private List<String> userIds;

    private Object selectUsers;

}
