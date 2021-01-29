package com.jit.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: SysRoleDTO
 * @Author: zengxin_miao
 * @Date: 2021.01.25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorClaimDTO {

    private String id;

    private String hostId;

    private String hostName;

    private String triggerId;

    private String problemId;

    private String problemName;

    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime problemCreate;

    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime claimTime;

    private String claimOpinion;

    private String claimUserId;

    private String claimRoleId;

    private int isClaim;

    private String severity;

    private String ns;

    private int isRegister;

    private int isResolve;

    private String problemHandleTime;

    @JsonDeserialize(using = CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime resolveTime;

}
