package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jit.zabbix.client.utils.CustomJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * @Description: table assets entity
 * @Author: yongbin_jiang
 * @Date: 2020/06/16 13:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetsParams {
    private String name;

    private String type;

    private String number;

    private String state;

    private String gbType;

    private String ip;

    private String backupIp;

    private int amount;

    private String belongsDept;

    private String belongsPerson;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime registerDate;

    private String registrant;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime updateDate;

    private String location;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime logoutDate;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime registerStartDate;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime registerEndDate;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime dateRecorded;

    private String worth;

    private String acquisitionMode;

    private String userDepartment;

    private String user;

    private String objectClassification;

    private String sn;

    private String brand;

    private String productModel;
}
