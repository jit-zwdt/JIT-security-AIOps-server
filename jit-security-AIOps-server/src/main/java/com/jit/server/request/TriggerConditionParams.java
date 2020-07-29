package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: table assets entity
 * @Author: Feng Qing
 * @Date: 2020/07/28 10:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TriggerConditionParams {

    private String hostId;
    private String triggerId;
    private String description;
    private String status;

    private Integer level; // use min_severity
//    private Date dateFrom;
//    private Date DateTill;
//    private String ip;
    private String keyWord; // use host
//    private String type;
    private Boolean unconfirmed; // use withUnacknowledgedEvents
    private Boolean confirmed; // use withAcknowledgedEvents
    private Boolean maintain; // use maintenance
//    private Boolean alert;
}