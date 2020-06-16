package com.jit.server.pojo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Description: table assets entity
 * @Author: yongbin_jiang
 * @Date: 2020/06/16 13:01
 */
@Data
@Entity
@Table(name = "assets")
public class AssetsEntity {
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "user")
    private String user;

    @Column(name = "type")
    private String type;

    @Column(name = "gmt_register")
    private Timestamp gmtRegister;

    @Column(name = "gmt_cancellation")
    private Timestamp gmtCancellation;

    @Column(name = "delflag")
    private String delflag;

    @Column(name = "gmt_create")
    private Timestamp gmtCreate;

    @Column(name = "gmt_modified")
    private Timestamp gmtModified;
}
