package com.jit.server.util;

/**
 * @Description: Const Class
 * @Author: zengxin_miao
 * @Date: 2020.07.31
 */
public class ConstUtil {

    /**
     * divider
     */
    public static final String PROP_DIVIDER = "=#=";

    /**
     * max limit
     */
    public static final int LIMIT_MAX = 2147483647;

    /**
     * is_deleted (是否删除 1:表示删除;0:表示未删除)
     */
    public static final int IS_DELETED = 1;

    /**
     * is_deleted (是否删除 1:表示删除;0:表示未删除)
     */
    public static final int IS_NOT_DELETED = 0;

    /**
     * 状态（0：正常；1：停止）
     */
    public static final int STATUS_NORMAL = 0;

    /**
     * 状态（0：正常；1：停止）
     */
    public static final int STATUS_STOP = 1;

    /**
     * 是否解决 （0：未解决；1：解决）
     */
    public static final int UN_RESOLVE = 0;

    /**
     * 是否解决 （0：未解决；1：解决）
     */
    public static final int RESOLVED = 1;

    /**
     * 认证header
     */
    public static final String HEADER_STRING = "Authorization";

    /**
     * authMap
     */
    public static final String AUTH_MAP = "authMap";

    /**
     * 状态（0：为启用；1：启用）
     */
    public static final int STATUS_UNUSED = 0;

    /**
     * 状态（0：为启用；1：启用）
     */
    public static final int STATUS_USING = 1;

    /**
     * jit 主机群组
     */
    public static final String HOSTGROUP_NAME = "正元";

    /**
     * PID 父类型节点 0
     */
    public static final String PID_0 = "0";
}
