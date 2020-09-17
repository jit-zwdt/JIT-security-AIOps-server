package com.jit.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

/**
 * @Description: TimmerConfig
 * @Author: zengxin_miao
 * @Date: 2020/09/17
 */
@Component
@Slf4j
public class ScheduleTaskConfig {

    /**
     * 接受任务的返回结果
     */
    private ScheduledFuture<?> future;

    @Autowired
    @Lazy
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 自定义的ThreadPoolTaskScheduler
     */
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        return threadPoolTaskScheduler;
    }


    /**
     * 启动定时任务
     *
     * @return
     */
    public boolean startCron(String cron) {
        boolean flag = false;
        //从数据库动态获取执行周期
        //String cron = "0/2 * * * * ? ";
        future = threadPoolTaskScheduler.schedule(new CheckModelFile(cron), new CronTrigger(cron));
        if (future != null) {
            flag = true;
            log.info("定时check训练模型文件,任务启动成功！！！");
        } else {
            log.info("定时check训练模型文件,任务启动失败！！！");
        }
        return flag;
    }

    /**
     * 停止定时任务
     *
     * @return
     */
    public boolean stopCron() {
        boolean flag = false;
        if (future != null) {
            boolean cancel = future.cancel(true);
            if (cancel) {
                flag = true;
                log.info("定时check训练模型文件,任务停止成功！！！");
            } else {
                log.info("定时check训练模型文件,任务停止失败！！！");
            }
        } else {
            flag = true;
            log.info("定时check训练模型文件，任务已经停止！！！");
        }
        return flag;
    }


    class CheckModelFile implements Runnable {
        private String cron;

        public CheckModelFile(String cron) {
            this.cron = cron;
        }

        @Override
        public void run() {
            //编写你自己的业务逻辑
            log.info("模型文件检查完毕！！！" + this.cron + "  " + LocalDateTime.now());
        }
    }
}
