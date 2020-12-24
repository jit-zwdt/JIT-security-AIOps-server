package com.jit.server.aspect;

import com.alibaba.fastjson.JSON;
import com.jit.server.annotation.AutoLog;
import com.jit.server.pojo.SysLogEntity;
import com.jit.server.service.SysLogService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.IPUtils;
import com.jit.server.util.RequestHolder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 系统日志，切面处理类
 *
 * @Author: zengxin_miao
 * @Date: 2020年12月23日 10:50:04
 */
@Aspect
@Component
public class AutoLogAspect {
    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.jit.server.annotation.AutoLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLogEntity = new SysLogEntity();
        AutoLog syslog = method.getAnnotation(AutoLog.class);
        if (syslog != null) {
            //注解上的描述,操作日志内容
            sysLogEntity.setLogContent(syslog.value());
            sysLogEntity.setLogType(syslog.logType());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLogEntity.setMethod(className + "." + methodName + "()");

        //设置操作类型
        if (sysLogEntity.getLogType() == ConstLogUtil.LOG_TYPE_OPERATION) {
            sysLogEntity.setOperationType(getOperationType(methodName));
        }

        //获取request
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        //设置IP地址
        sysLogEntity.setIp(IPUtils.getIpAddr(request));
        //设置请求路径
        sysLogEntity.setRequestUrl(request.getRequestURL().toString());
        //设置请求类型
        sysLogEntity.setRequestType(request.getMethod());

        // 参数名数组
        String[] parameterNames = ((MethodSignature) signature).getParameterNames();

        // 构造参数组集合
        List<Object> argList = new ArrayList<>();
        for (Object arg : joinPoint.getArgs()) {
            // request/response无法使用toJSON
            if (arg instanceof HttpServletRequest) {
                argList.add("request");
            } else if (arg instanceof HttpServletResponse) {
                argList.add("response");
            } else {
                argList.add(JSON.toJSON(arg));
            }
        }

        //设置请求参数
        //为了安全，不记录登录信息
        if (!"login".equals(methodName)) {
            sysLogEntity.setRequestParam(ArrayUtils.toString(parameterNames, ",") + "->" + argList.toString());
        }

        //获取登录用户信息
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (StringUtils.isNotBlank(name)) {
            sysLogEntity.setUserUsername(name);
            sysLogEntity.setUserName(sysLogService.getUserName(name));

        }
        //耗时
        sysLogEntity.setCostTime(time);
        sysLogEntity.setGmtCreate(LocalDateTime.now());
        sysLogEntity.setCreateBy(sysLogService.getUserId(name));

        //保存系统日志
        sysLogService.saveOrUpdateLog(sysLogEntity);
    }

    /**
     * 获取操作类型
     */
    private int getOperationType(String methodName) {
        methodName = methodName.toLowerCase();
        if (methodName.startsWith("get")) {
            return ConstLogUtil.OPERATION_TYPE_R;
        }
        if (methodName.startsWith("add")) {
            return ConstLogUtil.OPERATION_TYPE_C;
        }
        if (methodName.startsWith("update")) {
            return ConstLogUtil.OPERATION_TYPE_U;
        }
        if (methodName.startsWith("delete")) {
            return ConstLogUtil.OPERATION_TYPE_D;
        }
        if (methodName.startsWith("import")) {
            return ConstLogUtil.OPERATION_TYPE_IMP;
        }
        if (methodName.startsWith("export")) {
            return ConstLogUtil.OPERATION_TYPE_EXP;
        }
        if (methodName.startsWith("upload")) {
            return ConstLogUtil.OPERATION_TYPE_UPLOAD;
        }
        if (methodName.startsWith("download")) {
            return ConstLogUtil.OPERATION_TYPE_DOWNLOAD;
        }
        return ConstLogUtil.OPERATION_TYPE_UNDEFINE;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) throws Exception {

        String errorLog = getStackTrace(e).substring(0, 1000);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLogEntity = new SysLogEntity();

        sysLogEntity.setErrorLog(errorLog);
        AutoLog syslog = method.getAnnotation(AutoLog.class);
        if (syslog != null) {
            //注解上的描述,操作日志内容
            sysLogEntity.setLogContent(syslog.value());
            sysLogEntity.setLogType(syslog.logType());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLogEntity.setMethod(className + "." + methodName + "()");

        //设置操作类型
        if (sysLogEntity.getLogType() == ConstLogUtil.LOG_TYPE_OPERATION) {
            sysLogEntity.setOperationType(getOperationType(methodName));
        }

        //获取request
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        //设置IP地址
        sysLogEntity.setIp(IPUtils.getIpAddr(request));
        //设置请求路径
        sysLogEntity.setRequestUrl(request.getRequestURL().toString());
        //设置请求类型
        sysLogEntity.setRequestType(request.getMethod());

        // 参数名数组
        String[] parameterNames = ((MethodSignature) signature).getParameterNames();

        // 构造参数组集合
        List<Object> argList = new ArrayList<>();
        for (Object arg : joinPoint.getArgs()) {
            // request/response无法使用toJSON
            if (arg instanceof HttpServletRequest) {
                argList.add("request");
            } else if (arg instanceof HttpServletResponse) {
                argList.add("response");
            } else {
                argList.add(JSON.toJSON(arg));
            }
        }

        //设置请求参数
        //为了安全，不记录登录信息
        if (!"login".equals(methodName)) {
            sysLogEntity.setRequestParam(ArrayUtils.toString(parameterNames, ",") + "->" + argList.toString());
        }

        //获取登录用户信息
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (StringUtils.isNotBlank(name)) {
            sysLogEntity.setUserUsername(name);
            sysLogEntity.setUserName(sysLogService.getUserName(name));

        }
        //耗时
        sysLogEntity.setGmtCreate(LocalDateTime.now());
        sysLogEntity.setCreateBy(sysLogService.getUserId(name));

        //保存系统日志
        sysLogService.saveOrUpdateLog(sysLogEntity);


    }

    /**
     * 获取堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
