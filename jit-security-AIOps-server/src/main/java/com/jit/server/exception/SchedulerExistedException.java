package com.jit.server.exception;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/10/13
 */
public class SchedulerExistedException extends Exception {

    /**
     * Constructs a <code>SchedulerExistedException</code> without a detail message.
     */
    public SchedulerExistedException() {
        super();
    }

    /**
     * Constructs a <code>SchedulerExistedException</code> with a detail message.
     *
     * @param s the detail message.
     */
    public SchedulerExistedException(String s) {
        super(s);
    }
}
