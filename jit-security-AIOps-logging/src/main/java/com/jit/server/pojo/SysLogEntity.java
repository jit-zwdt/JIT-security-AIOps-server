package com.jit.server.pojo;

/**
 * @Description table sys_log: table entity
 * @author zengxin_miao
 * @Date: 2020/12/23 11:34:53
 */

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_log")
public class SysLogEntity {

    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 日志类型 0:登录日志;1:操作日志;2:错误日志;
     */
    @Column(name = "log_type")
    private int logType;

    /**
     * 是否删除 0：未删除；1：已删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;

    /**
     * 日志内容
     */
    @Column(name = "log_content")
    private String logContent;

    /**
     * 操作日志类型 0:未定义;1:添加;2:查询;3:修改;4:删除;5:导入;6:导出;7:上传;8:下载
     */
    @Column(name = "operation_type")
    private int operationType;

    /**
     * 操作人账号
     */
    @Column(name = "user_username")
    private String userUsername;

    /**
     * 操作人姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * IP
     */
    @Column(name = "ip")
    private String ip;

    /**
     * IP来源
     */
    @Column(name = "ip_from")
    private String ipFrom;

    /**
     * 请求方法
     */
    @Column(name = "method")
    private String method;

    /**
     * 请求路径
     */
    @Column(name = "request_url")
    private String requestUrl;

    /**
     * 请求参数
     */
    @Column(name = "request_param")
    private String requestParam;

    /**
     * 请求类型
     */
    @Column(name = "request_type")
    private String requestType;

    /**
     * 耗时
     */
    @Column(name = "cost_time")
    private long costTime;

    /**
     * 错误日志
     */
    @Column(name = "error_log")
    private String errorLog;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    @Column(name = "gmt_modified")
    private LocalDateTime gmtModified;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

}
