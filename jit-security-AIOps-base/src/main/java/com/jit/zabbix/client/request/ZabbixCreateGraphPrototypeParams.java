package com.jit.zabbix.client.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.graph.GraphItem;
import com.jit.zabbix.client.model.graph.GraphPrototype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixCreateGraphPrototypeParams{

    @JsonProperty("graphid")
    protected String graphid;

    protected Integer height;

    protected String name;

    protected Integer width;

    /**
     图形原型布局类型 .

     可能值:
     0 - (默认) 常规;
     1 - 堆积图;
     2 - 饼图;
     3 - 分散饼图.
     */
    @JsonProperty("graphtype")
    protected Integer graphType;

    @JsonProperty("percent_left")
    protected Float percentLeft;

    @JsonProperty("percent_right")
    protected Float percentRight;
    /*
    	是否使用3D形式显示被发现的饼图和分散饼图.
        可能值:
        0 - (默认) 以2D形式展示;
        1 - 以3D形式展示.
     */
    @JsonProperty("show_3d")
    protected Integer show3d;

    /*
    * 是否在被发现的图表上显示图例.
    可能值:
    0 - 隐藏;
    1 - (默认) 显示.*/
    @JsonProperty("show_legend")
    protected Integer showLegend;

    @JsonProperty("show_work_period")
    protected Integer showWorkPeriod;

    @JsonProperty("templateid")
    protected Integer templateId;

    protected Float yaxismax;

    protected Float yaxismin;

    @JsonProperty("ymax_itemid")
    protected String ymaxItemid;

    @JsonProperty("ymax_type")
    protected Integer ymaxType;

    @JsonProperty("ymin_itemid")
    protected String yminItemid;

    @JsonProperty("ymin_type")
    protected Integer yminType;

    private ArrayList<GraphItem> gitems;

}
