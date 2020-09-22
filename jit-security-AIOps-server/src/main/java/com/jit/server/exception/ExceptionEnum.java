package com.jit.server.exception;

public enum ExceptionEnum {
    PAGE_NOT_FOUND(404, "页面不存在"),
    NO_AUTH(403, "权限不足"),
    QUERY_DATA_EXCEPTION(0400, "数据库连接异常"),
    LOGIN_EXCEPTION(1001, "登录异常"),
    LOGIN_PASSWORD_OR_USRNAME_EXCEPTION(1002, "用户名或密码不正确"),
    LOGIN_VERIFICATION_CODE_EXCEPTION(1003, "验证码不正确"),
    LOGIN_VERIFICATION_CODE_EXPIRES_EXCEPTION(1004, "验证码失效"),
    ACCOUNT_DISABLED_EXPIRES_EXCEPTION(1005, "账户已禁用"),
    SCHEDULER_CREATE_EXCEPTION(3001, "创建定时任务失败"),
    SCHEDULER_USE_CLASS_EXCEPTION(3002, "后台找不到定时任务执行类名"),
    SCHEDULER_USE_METHOD_EXCEPTION(3003, "后台找不到定时任务执行方法名"),
    SCHEDULER_CRON_EXPRESSION_EXCEPTION(3004, "cron表达式不正确"),
    INNTER_EXCEPTION(500, "系统内部异常"),
    DATABASE_EXCEPTION(5001, "数据库连接异常"),
    FILE_NOT_FOUND_EXCEPTION(5002, "文件不存在"),
    FILE_RW_EXCEPTION(5003, "文件读写异常"),
    TOKEN_EXCEPTION(5004, "令牌异常"),
    OPERATION_EXCEPTION(5005, "操作异常"),
    Duplicate_EXCEPTION(5006, "重复记录"),
    TOKEN_EXPIRED_EXCEPTION(5007, "令牌过期"),
    PARAMS_NULL_EXCEPTION(6001, "接口参数为空"),
    RESULT_NULL_EXCEPTION(6002, "记录不存在");


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
