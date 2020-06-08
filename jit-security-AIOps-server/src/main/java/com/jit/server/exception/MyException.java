package com.jit.server.exception;

public class MyException extends RuntimeException{

    private ExceptionEnum exceptionEnum;

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public MyException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}
