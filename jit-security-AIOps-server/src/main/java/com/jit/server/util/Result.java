package com.jit.server.util;


import com.jit.server.exception.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;
    private static final String SUCCESS_MESSAGE = "request successful";


    private Result(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.msg = exceptionEnum.getMessage();
    }

    public static Result SUCCESS(Object t) {
        Result result = new Result(1, SUCCESS_MESSAGE, t);
        return result;
    }

    public static Result ERROR(ExceptionEnum exceptionEnum) {
        Result result = new Result(exceptionEnum);
        return result;
    }
}
