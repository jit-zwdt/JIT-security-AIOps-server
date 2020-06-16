package com.jit.server.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @JsonDeserialize(using= CustomJsonSerializer.TimestampStrDeserializer.class)
    @Column(name = "asset_register_date")
    private Timestamp assetRegisterDate;

    @Column(name = "asset_registrant")
    private String assetRegistrant;

    @Column(name = "asset_update_date")
    private Timestamp assetUpdateDate;

    @Column(name = "asset_location")
    private String assetLocation;

    @Column(name = "asset_logout_date")
    private Timestamp assetLogoutDate;

    @Column(name = "is_deleted")
    private String isDeleted;

    @Column(name = "gmt_create")
    private Timestamp gmtCreate;

    @Column(name = "gmt_modified")
    private Timestamp gmtModified;
}
