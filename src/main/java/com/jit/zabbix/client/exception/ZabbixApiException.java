package com.jit.zabbix.client.exception;

import com.jit.zabbix.client.response.JsonRPCError;

/**
 * @author Mamadou Lamine NIANG
 **/

public class ZabbixApiException extends Exception {

    private JsonRPCError error;

    public ZabbixApiException(JsonRPCError error) {
        super(String.format("Error: %d\nMessage:%s\nData:%s'", error.getCode(), error.getMessage(), error.getData()));
        this.error = error;
    }

    public ZabbixApiException(String message) {
        super(message);
    }

    public ZabbixApiException(int httpCode) {
        super(String.format("Api call failed with code %d.", httpCode));
    }

    public ZabbixApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonRPCError getError() {
        return error;
    }

    public void setError(JsonRPCError error) {
        this.error = error;
    }
}
