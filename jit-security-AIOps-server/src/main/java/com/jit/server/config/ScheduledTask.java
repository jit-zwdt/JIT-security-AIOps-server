package com.jit.server.config;

import java.util.concurrent.ScheduledFuture;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/09/21
 */
public final class ScheduledTask {

    public volatile ScheduledFuture<?> future;

    /**
     * 取消定时任务
     */
    public boolean cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            return future.cancel(true);
        }
        return false;
    }
}