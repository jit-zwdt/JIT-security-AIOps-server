package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jit.zabbix.client.model.graph.GraphPrototype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author: lancelot
 * @Date: 2020/07/28 14:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateGraphPrototypeParams extends GraphPrototype {

    private List<Map<String,Integer>> gitems;

}
