package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private List<String> triggerIds;
}