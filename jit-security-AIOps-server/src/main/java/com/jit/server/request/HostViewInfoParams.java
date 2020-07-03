package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: HostViewInfoParams
 * @Author: jian_liu
 * @Date: 2020-6-23 15:01:15
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HostViewInfoParams {
    private String id;
    private String objectName;
    private String hostid;
    private String hostIp;
    private String agentIp;
    private String snmpIp;
    private String enableMonitor;
    private String groupId;
    private String hostLabel;
    private String remark;
    private String type;
    private String subtype;
    private String businessName;
    private String templatesId;
    private String typeId;
    private String subtypeId;
}
