package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

/**
 * @Author: lancelot
 * @Date: 2020/07/28 11:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GraphPrototypeParams {

    private List<String> discoveryids;
    private List<String> hostids;
    private String status;
    private String name;
    private List<String> itemids;
    private List<String> groupids;
    private List<String> templateids;
    private List<String> graphids;


}
