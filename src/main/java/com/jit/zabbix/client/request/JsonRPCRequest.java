package com.jit.zabbix.client.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Represents a request object as defined by the <a href="https://www.jsonrpc.org/specification#request_object">JSON-RPC 2.0 specification</a>.
 *
 * @author Mamadou Lamine NIANG
 **/

public class JsonRPCRequest {

    private final String jsonrpc = "2.0";
    @NotBlank(message = "Please provide the method.")
    private String method;
    @NotNull(message = "Please provide the parameters of the request.")
    private Object params;
    private String id = UUID.randomUUID().toString();
    private String auth;

    public JsonRPCRequest() {
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "JsonRPCRequest(jsonrpc=" + getJsonrpc() + ", method=" + getMethod() + ", params=" + getParams() + ", id=" + getId() + ", auth=" + getAuth() + ")";
    }
}
