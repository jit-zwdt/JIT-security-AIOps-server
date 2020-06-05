package com.jit.zabbix.client.service;


import com.jit.zabbix.client.autoconfigure.ZabbixApiProperties;
import com.jit.zabbix.client.dto.ZabbixAuthDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;


/**
 * Main service making requests to Zabbix API.
 * @author Mamadou Lamine NIANG
 **/
@Service
@Slf4j
public class ZabbixApiService {

    private final ZabbixApiProperties properties;
    private final JsonMapper jsonMapper;
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public ZabbixApiService(ZabbixApiProperties zabbixApiProperties, JsonMapper jsonMapper, RestTemplate restTemplate) {
        this.properties = zabbixApiProperties;
        this.jsonMapper = jsonMapper;
        this.restTemplate = restTemplate;
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public com.jit.zabbix.client.response.JsonRPCResponse call(String method, Object params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = new com.jit.zabbix.client.request.JsonRPCRequest();
        request.setAuth(auth);
        request.setParams(params);
        request.setMethod(method);
        return call(request);
    }

    /**
     * Makes a call to Zabbix API
     * @param request               The request object.
     * @return                      A JSON RPC 2.0 response object.
     * @throws ZabbixApiException   When the response status is not 200 or the API returned an error.
     */
    public com.jit.zabbix.client.response.JsonRPCResponse call(@Valid com.jit.zabbix.client.request.JsonRPCRequest request) throws ZabbixApiException {
        HttpEntity<com.jit.zabbix.client.request.JsonRPCRequest> httpEntity = new HttpEntity<>(request, headers);
        String apiUrl = properties.getUrl() + ZabbixApiUtils.API_ENDPOINT;
        log.debug("Making request to {} with body: {}", apiUrl, request);
        try {
            ResponseEntity<com.jit.zabbix.client.response.JsonRPCResponse> response = restTemplate.postForEntity(apiUrl, httpEntity, com.jit.zabbix.client.response.JsonRPCResponse.class);
            if(response.getStatusCodeValue() != 200) {
                throw new ZabbixApiException(response.getStatusCodeValue());
            }
            if(!response.hasBody()) {
                throw new ZabbixApiException("Empty body received!");
            }
            com.jit.zabbix.client.response.JsonRPCResponse body = response.getBody();
            if(Objects.requireNonNull(body).isError()) {
                throw new ZabbixApiException(body.getError());
            }
            return body;
        } catch (RestClientException e) {
            throw new ZabbixApiException("Error making request to Zabbix Server", e);
        }
    }

    /**
     * Gets an authentication token from Zabbix
     * @param user                  The Zabbix user
     * @param password              The password
     * @return                      The auth token
     * @throws ZabbixApiException   When the response status is not 200 or the API returned an error.
     */
    public String authenticate(String user, String password) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = new com.jit.zabbix.client.request.JsonRPCRequest();
        request.setMethod("user.login");
        request.setParams(new ZabbixAuthDTO(user, password));

        com.jit.zabbix.client.response.JsonRPCResponse response = call(request);

        return jsonMapper.getObject(response.getResult(), String.class);
    }
}
