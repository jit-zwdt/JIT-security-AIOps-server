package com.jit.zabbix.client.utils;

import com.jit.zabbix.client.model.IZabbixMethod;

/**
 * @author Mamadou Lamine NIANG
 **/
public final class ZabbixApiUtils {

    public static final String API_ENDPOINT = "/api_jsonrpc.php";

    private ZabbixApiUtils() {
    }

    public static com.jit.zabbix.client.request.JsonRPCRequest buildRequest(IZabbixMethod method, Object params, String auth) {
        com.jit.zabbix.client.request.JsonRPCRequest request = new com.jit.zabbix.client.request.JsonRPCRequest();
        request.setMethod(method.getValue());
        request.setAuth(auth);
        request.setParams(params);
        return request;
    }
}
