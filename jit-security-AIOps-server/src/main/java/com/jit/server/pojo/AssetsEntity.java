package com.jit.server.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "asset_type")
    private String assetType;

    @Column(name = "asset_number")
    private String assetNumber;

    @Column(name = "asset_state")
    private String assetState;

    @Column(name = "asset_amount")
    private int assetAmount;

    @Column(name = "asset_belongs_dept")
    private String assetBelongsDept;

    @Column(name = "asset_belongs_person")
    private String assetBelongsPerson;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    @Column(name = "asset_register_date")
    private LocalDateTime assetRegisterDate;

    @Column(name = "asset_registrant")
    private String assetRegistrant;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    @Column(name = "asset_update_date")
    private LocalDateTime assetUpdateDate;

    @Column(name = "asset_location")
    private String assetLocation;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    @Column(name = "asset_logout_date")
    private LocalDateTime assetLogoutDate;

    @Column(name = "is_deleted")
    private String isDeleted;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    @Column(name = "gmt_create")
    private LocalDateTime gmtCreate;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    @Column(name = "gmt_modified")
    private LocalDateTime gmtModified;
}
