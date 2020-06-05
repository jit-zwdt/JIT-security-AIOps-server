package com.jit.zabbix.client.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represents a response object as defined by the <a href="https://www.jsonrpc.org/specification#response_object">JSON-RPC 2.0 specification</a>.
 *
 * @author Mamadou Lamine NIANG
 **/

public class JsonRPCResponse {

    private final String jsonrpc = "2.0";

    private JsonNode result;

    private JsonRPCError error;

    private String id;

    public void setResult(JsonNode result) {
        this.result = result;
    }

    public void setError(JsonRPCError error) {
        this.error = error;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "JsonRPCResponse(jsonrpc=" + getJsonrpc() + ", result=" + getResult() + ", error=" + getError() + ", id=" + getId() + ")";
    }

    public JsonRPCResponse() {
    }

    public String getJsonrpc() {
        getClass();
        return "2.0";
    }

    public JsonNode getResult() {
        return this.result;
    }

    public JsonRPCError getError() {
        return this.error;
    }

    public String getId() {
        return this.id;
    }

    public boolean isError() {
        return (this.error != null);
    }

}
