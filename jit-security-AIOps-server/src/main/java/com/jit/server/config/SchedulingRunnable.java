package com.jit.server.config;

import com.jit.server.util.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Description: SchedulingRunnable
 * @Author: zengxin_miao
 * @Date: 2020/09/21
 */
@Slf4j
public class SchedulingRunnable implements Runnable {

    private String className;

    private String methodName;

    private String params;

    public SchedulingRunnable(String className, String methodName) {
        this(className, methodName, null);
    }

    public SchedulingRunnable(String className, String methodName, String params) {
        this.className = className;
        this.methodName = methodName;
        this.params = params;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("定时任务开始执行 - className：{}，方法：{}，参数：{}", className, methodName, params);
        }

        try {
            Object target = ApplicationContextUtils.getBeanByClassName(className);

            Method method;
            if (params != null && params.trim().length() > 0) {
                method = ReflectionUtils.findMethod(target.getClass(), methodName, String.class);
            } else {
                method = target.getClass().getDeclaredMethod(methodName);
            }

            ReflectionUtils.makeAccessible(method);
            if (null != params && params.trim().length() > 0) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception ex) {
            log.error(String.format("定时任务执行异常 - className：%s，方法：%s，参数：%s ", className, methodName, params), ex);
        }

        long times = System.currentTimeMillis() - startTime;
        if (log.isDebugEnabled()) {
            log.debug("定时任务执行结束 - className：{}，方法：{}，参数：{}，耗时：{} 毫秒", className, methodName, params, times);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SchedulingRunnable that = (SchedulingRunnable) o;
        if (params == null) {
            return className.equals(that.className) &&
                    methodName.equals(that.methodName) &&
                    that.params == null;
        }

        return className.equals(that.className) &&
                methodName.equals(that.methodName) &&
                params.equals(that.params);
    }


    @Override
    public int hashCode() {
        if (params == null) {
            return Objects.hash(className, methodName);
        }

        return Objects.hash(className, methodName, params);
    }
}