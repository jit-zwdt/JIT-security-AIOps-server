package com.jit.server.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Description: SampleJob
 * @Author: zengxin_miao
 * @Date: 2020/09/11
 */

@Slf4j
@Setter
@Component
@DisallowConcurrentExecution
public class SampleJob implements Job {

    private String jsonParam;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info(String.format(jobExecutionContext.getJobDetail().getKey() + " 普通定时任务 SampleJob !  时间:" + LocalDateTime.now()) + " 参数：" + this.jsonParam);
    }
}
