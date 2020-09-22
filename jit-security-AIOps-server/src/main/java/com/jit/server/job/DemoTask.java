package com.jit.server.job;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/09/21
 */
@Component("demoTask")
public class DemoTask {

    public void taskWithParams(String param) {
        System.out.println("这是有参示例任务：时间:" + LocalDateTime.now() + " 参数：" + param);

    }

    public void taskNoParams() {
        System.out.println("这是无参示例任务");
    }
}