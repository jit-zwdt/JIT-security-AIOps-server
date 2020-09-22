package com.jit.server.exception;

/**
 * @Description: CronExpression
 * @Author: zengxin_miao
 * @Date: 2020/09/18
 */
public class CronExpression extends ReflectiveOperationException {

    /**
     * Constructs a <code>NoSuchMethodException</code> without a detail message.
     */
    public CronExpression() {
        super();
    }

    /**
     * Constructs a <code>NoSuchMethodException</code> with a detail message.
     *
     * @param s the detail message.
     */
    public CronExpression(String s) {
        super(s);
    }
}
