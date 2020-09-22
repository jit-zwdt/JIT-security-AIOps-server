package com.jit.server.config;

import com.jit.server.exception.CronExpression;
import com.jit.server.pojo.SysScheduleTaskEntity;
import com.jit.server.service.SysScheduleTaskService;
import com.jit.server.util.ApplicationContextUtils;
import com.jit.server.util.CronValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/09/21
 */
@Component
@Slf4j
public class CronTaskRegistrar implements DisposableBean {

    @Autowired
    private SysScheduleTaskService sysScheduleTaskService;

    private final Map<String, Runnable> taskMap = new ConcurrentHashMap<>(16);
    private final Map<String, ScheduledTask> scheduledTaskMap = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;

    public TaskScheduler getScheduler() {
        return this.taskScheduler;
    }

    /**
     * 初始化已有任务
     */
    @Bean
    public void initScheduleTasks() {
        try {
            List<SysScheduleTaskEntity> sysScheduleTaskEntityList = sysScheduleTaskService.getScheduleTaskList();
            if (sysScheduleTaskEntityList == null) {
                log.info("待初始化任务列表为空！");
            } else {
                for (SysScheduleTaskEntity s : sysScheduleTaskEntityList) {
                    this.addCronTask(s.getJobClassName(), s.getJobMethodName(), s.getCronExpression(), s.getJsonParam());
                }
            }
        } catch (Exception e) {
            log.error("初始化任务失败", e);
            e.printStackTrace();
        }
    }


    /**
     * 新增定时任务
     *
     * @param className
     * @param methodName
     * @param cronExpression
     * @param param
     * @throws Exception
     */
    public void addCronTask(String className, String methodName, String cronExpression, String param) throws Exception {
        //校验className和methodName
        Object bean = ApplicationContextUtils.getBeanByClassName(className);
        //校验methodName
        Method method = ReflectionUtils.findMethod(bean.getClass(), methodName, String.class);
        if (method == null) {
            throw new NoSuchMethodException("class:" + className + ",method:" + methodName);
        }
        //利用quartz校验cron，new CronTrigger 校验不全
        if (!CronValidate.isValidExpression(cronExpression)) {
            throw new CronExpression("cron is not valid");
        }
        String key = className + "." + methodName + "(" + cronExpression + ")";
        SchedulingRunnable task = new SchedulingRunnable(className, methodName, param);
        addCronTask(key, new CronTask(task, cronExpression));
    }

    private void addCronTask(String key, CronTask cronTask) {
        if (cronTask != null) {
            Runnable task = cronTask.getRunnable();
            if (this.taskMap.containsKey(key)) {
                taskMap.remove(key);
            }
            if (this.scheduledTaskMap.containsKey(key)) {
                removeCronTask(key);
            }
            this.taskMap.put(key, task);
            this.scheduledTaskMap.put(key, scheduleCronTask(cronTask));
        }
    }

    /**
     * 移除定时任务
     *
     * @param key
     */
    public boolean removeCronTask(String key) {
        ScheduledTask scheduledTask = this.scheduledTaskMap.remove(key);
        if (scheduledTask != null) {
            this.taskMap.remove(key);
            return scheduledTask.cancel();
        }
        return false;
    }

    public ScheduledTask scheduleCronTask(CronTask cronTask) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduledTask;
    }


    @Override
    public void destroy() {
        for (ScheduledTask task : this.scheduledTaskMap.values()) {
            task.cancel();
        }
        this.scheduledTaskMap.clear();
        this.taskMap.clear();
    }
}
