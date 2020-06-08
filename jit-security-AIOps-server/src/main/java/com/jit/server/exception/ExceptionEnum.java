package com.jit.server.exception;

public enum ExceptionEnum {
    PAGE_NOT_FOUND(404,"页面不存在"),
    NO_AUTH(403,"权限不足"),
    DATABASE_EXCEPTION(5001,"数据库连接异常"),
    FILE_NOT_FOUND_EXCEPTION(5002,"文件不存在"),
    FILE_RW_EXCEPTION(5003,"文件读写异常"),
    TOKEN_EXCEPTION(5004,"令牌异常"),
    LOGIN_EXCEPTION(1001,"登录异常"),
    INNTER_EXCEPTION(500,"系统内部异常"),
    OPERATION_EXCEPTION(5005,"操作异常"),
    Duplicate_EXCEPTION(5006,"重复记录");
    ;

    private int code;

    private String message;

    ExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
