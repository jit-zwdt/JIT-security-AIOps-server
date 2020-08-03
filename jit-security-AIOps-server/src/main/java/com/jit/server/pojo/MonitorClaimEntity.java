package com.jit.server.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "monitor_claim")
public class MonitorClaimEntity {
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "host_id")
    private String hostId;

    @Column(name = "host_name")
    private String hostName;

    @Column(name = "trigger_id")
    private String triggerId;

    @Column(name = "problem_id")
    private String problemId;

    @Column(name = "problem_name")
    private String problemName;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    @Column(name = "problem_create")
    private LocalDateTime problemCreate;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    @Column(name = "claim_time")
    private LocalDateTime claimTime;

    @Column(name = "claim_opinion")
    private String claimOpinion;

    @Column(name = "claim_user_id")
    private String claimUserId;

    @Column(name = "claim_role_id")
    private String claimRoleId;

    @Column(name = "is_claim")
    private int isClaim;

    @Column(name = "problem_severity")
    private String severity;

    @Column(name = "problem_ns")
    private String ns;
}
