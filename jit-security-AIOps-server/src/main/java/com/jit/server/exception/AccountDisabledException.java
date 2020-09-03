package com.jit.server.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * @Description: account disabled exception
 * @Author: zengxin_miao
 * @Date: 2020/09/03
 */
public class AccountDisabledException extends InternalAuthenticationServiceException {

    public AccountDisabledException(String msg) {
        super(msg);
    }

    public AccountDisabledException(String msg, Throwable t) {
        super(msg, t);
    }
}
