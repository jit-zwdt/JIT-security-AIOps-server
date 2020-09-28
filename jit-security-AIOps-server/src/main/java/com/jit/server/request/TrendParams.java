package com.jit.server.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: table assets entity
 * @Author: jian_liu
 * @Date: 2020/07/21 11:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrendParams {

    private List<String> itemids;
    private String timefrom;
    private String timetill;

    private String hostId;
    private String typeId;
    private String itemId;
    private String itemName;
    private String graphId;
    private String graphName;
    private String units;

}
