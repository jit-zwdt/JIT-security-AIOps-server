package com.jit.server.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/09/21
 */
@Component("demoTask")
@Slf4j
public class DemoTask {

    public void taskWithParams(String param) {
        log.info("这是有参示例任务：时间:{} 参数：{}", LocalDateTime.now(), param);
    }

    public void taskNoParams() {
        log.info("这是无参示例任务");
    }
}