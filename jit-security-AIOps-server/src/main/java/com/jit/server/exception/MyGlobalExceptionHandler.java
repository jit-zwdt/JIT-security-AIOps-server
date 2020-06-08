package com.jit.server.exception;

import com.jit.server.util.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class MyGlobalExceptionHandler {
    @ExceptionHandler({MyException.class})
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleException(HttpServletRequest request, Exception e) throws Exception {
        MyException exception = (MyException) e;
        return Result.ERROR(exception.getExceptionEnum());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    public Result baseException(HttpServletRequest request,Exception e) throws Exception{
        return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
    }
}
