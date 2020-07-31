package com.jit.zabbix.client.model.graph;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * Zabbix Graph Item object.
 *
 * @author lancelot
 * @see <a href="https://www.zabbix.com/documentation/4.0/zh/manual/api/reference/graphitem/object">Graph Prototype object</a>
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GraphItem {

    @JsonProperty("gitemid")
    protected String gItemId;

    protected String color;

    @JsonProperty("itemid")
    protected String itemId;

    /*
    * 监控项显示的值.

    可用值:
    1 - 最小值;
    2 - (默认) 平均值;
    4 - 最大值;
    7 - 所有值;
    9 - 最新的值，仅适用于饼图以及分散饼图.*/
    @JsonProperty("calc_fnc")
    protected Integer calcFnc;
    /**
     * 用于绘制图表监控的线形.
     *
     * 可用值:
     * 0 - (默认) 实线;
     * 1 - 面积图（填满的区域);
     * 2 - 粗实线;
     * 3 - 点;
     * 4 - 虚线;
     * 5 - 梯度线.
     */
    @JsonProperty("drawtype")
    protected Integer drawType;

    @JsonProperty("graphid")
    protected String graphId;

    protected Integer sortorder;

    protected Integer type;

    /*
    * 图表监控项的Y轴画在图表的那一侧:

    可用值:
    0 - (默认) 左侧;
    1 - 右侧.
    *
    * */
    protected Integer yaxisside;
}
