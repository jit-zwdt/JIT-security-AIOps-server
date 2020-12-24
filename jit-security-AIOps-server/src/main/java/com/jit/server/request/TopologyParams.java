package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TopologyParams
 * @Author: jian_lliu
 * @Date: 2020/12/23 13:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopologyParams {

    private String id;

    private String jsonParam;

    private String infoName;

    private String ip;

}
