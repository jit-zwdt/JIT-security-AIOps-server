package com.jit.server.util;

/**
 * 导出 XLS 文件常量
 * @author ALIENWARE <br />
 * 创建时间: 2020.11.23
 */
public class ExportXlsFileConst {

    /**
     * 故障解决报表的表标题
     */
    public static final String TROUBLESHOOTING_HEAD_NAME = "故障解决统计报表";

    /**
     * 故障解决报表的表头
     */
    public static final String[] TROUBLESHOOTING_TABLE_HEADER = {"序号","故障解决日期","故障名称","故障类型","处理角色","处理人","故障原因","处理过程","处理方式","故障持续时间","故障处理时长"};

    /**
     * 运维日报表标题
     */
    public static final String OPERATION_REPORT_HEAD_NAME = "运维日报";

    /**
     * 运维日报的表头
     */
    public static final String[] OPERATION_REPORT_TABLE_HEADER = {"类别","当日新增数","当日新增详情","本月总数"};

    /**
     * 运维日报数据的出现问题的表列第一个数据
     */
    public static final String OPERATION_REPORT_PROBLEMS = "出现问题";
    /**
     * 运维日报数据的认领问题的表列第一个数据
     */
    public static final String OPERATION_REPORT_CLAIM_PROBLEMS = "认领问题";
    /**
     * 运维日报数据的处理中问题的表列第一个数据
     */
    public static final String OPERATION_REPORT_HANDLING_PROBLEMS = "处理中问题";
    /**
     * 运维日报数据的解决问题的表列第一个数据
     */
    public static final String OPERATION_REPORT_SOLVE_PROBLEMS = "解决问题";

    /**
     * 通用的日期标注常量
     */
    public static final String DATE_CONST = "日期:";

    /**
     * 通用的运维人的标注常量
     */
    public static final String OPERATION_MAINTENANCE_PERSON = "运维人:";

    /**
     * 通用的时间的标注常量
     */
    public static final String TIME_CONST = "时间:";

    /**
     * 通用的负责人签字的标注常量
     */
    public static final String PERSON_IN_CHARGE  = "负责人(签字):";


}
