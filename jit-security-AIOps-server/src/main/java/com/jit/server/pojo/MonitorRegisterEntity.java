package com.jit.server.pojo;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Data
@Entity
@Table(name = "monitor_register")
public class MonitorRegisterEntity {
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "problem_type")
    private String problemType;

    @Column(name = "problem_reason")
    private String problemReason;

    @Column(name = "problem_id")
    private String problemId;

    @Column(name = "problem_solutions")
    private String problemSolution;

    @Column(name = "problem_process")
    private String problemProcess;

    @Column(name = "is_resolve")
    private int isResolve;

    @Column(name = "claim_id")
    private String claimId;

    /**
     * 删除标识 0：表示未删除， 1：表示删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private java.sql.Timestamp gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private java.sql.Timestamp gmtModified;
}
