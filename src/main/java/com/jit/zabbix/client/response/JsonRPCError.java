package com.jit.zabbix.client.response;

/**
 * Represents an response error object as defined by the <a href="https://www.jsonrpc.org/specification#error_object">JSON-RPC 2.0 specification</a>.
 *
 * @author Mamadou Lamine NIANG
 **/

public class JsonRPCError {
    private int code;

    private String message;

    private String data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonRPCError(code=" + getCode() + ", message=" + getMessage() + ", data=" + getData() + ")";
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getData() {
        return this.data;
    }


}
