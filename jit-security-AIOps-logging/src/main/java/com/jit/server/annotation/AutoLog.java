package com.jit.server.annotation;


import com.jit.server.util.ConstLogUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:系统日志注解
 * @Author: zengxin_miao
 * @Date: 2020年12月23日 10:50:04
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoLog {

    /**
     * 日志内容
     *
     * @return
     */
    String value() default "";

    /**
     * 日志类型
     *
     * @return 0:登录日志;1:操作日志;2:错误日志;
     */
    int logType();

    /**
     * 操作日志类型
     *
     * @return 0:未定义;1:添加;2:查询;3:修改;4:删除
     */
    int operationType() default ConstLogUtil.OPERATION_TYPE_UNDEFINE;
}
