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
    private String assetName;

    private String assetType;

    private String assetNumber;

    private String assetState;

    private int assetAmount;

    private String assetBelongsDept;

    private String assetBelongsPerson;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime assetRegisterDate;

    private String assetRegistrant;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime assetUpdateDate;

    private String assetLocation;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime assetLogoutDate;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime assetRegisterStartDate;

    @JsonDeserialize(using= CustomJsonSerializer.LocalDateTimeStrDeserializer.class)
    private LocalDateTime assetRegisterEndDate;
}
