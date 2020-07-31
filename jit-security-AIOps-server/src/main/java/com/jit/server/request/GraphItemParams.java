package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: lancelot
 * @Date: 2020/07/29 14:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GraphItemParams {

    private List<String> gitemids;
    private List<String> graphids;
    private List<String> itemids;
    private Integer type;
    private String status;
    private String name;


}
