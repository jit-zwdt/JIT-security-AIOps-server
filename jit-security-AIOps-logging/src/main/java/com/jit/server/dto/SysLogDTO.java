package com.jit.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: SysLogDTO
 * @Author: zengxin_miao
 * @Date: 2021/01/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysLogDTO {

    private String id;

    /**
     * 日志类型 0:登录日志;1:操作日志;2:错误日志;
     */
    private int logType;

    /**
     * 日志内容
     */
    private String logContent;

    /**
     * 操作日志类型 0:未定义;1:添加;2:查询;3:修改;4:删除;5:导入;6:导出;7:上传;8:下载
     */
    private int operationType;

    /**
     * 操作人账号
     */
    private String userUsername;

    /**
     * 操作人姓名
     */
    private String userName;

    /**
     * IP
     */
    private String ip;

    /**
     * IP来源
     */
    private String ipFrom;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 耗时
     */
    private long costTime;

    /**
     * 错误日志
     */
    private String errorLog;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;
}
