package com.jit.server.service;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.06.06
 */
public interface ZabbixAuthService {

    String getAuth() throws Exception;

    String getAuth(String key) throws Exception;
}
