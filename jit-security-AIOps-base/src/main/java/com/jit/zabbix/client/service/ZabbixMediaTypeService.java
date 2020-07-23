package com.jit.zabbix.client.service;


import com.jit.zabbix.client.dto.ZabbixGetMediaTypeDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.mediaType.MediaTypeMethod;
import com.jit.zabbix.client.request.ZabbixGetMediaTypeParams;
import com.jit.zabbix.client.utils.JsonMapper;
import com.jit.zabbix.client.utils.ZabbixApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Wrapper of {@link ZabbixMediaTypeService} for mediatype methods.
 *
 * @author zengxin_miao
 * @see ZabbixMediaTypeService
 **/
@Service
@Slf4j
public class ZabbixMediaTypeService {

    public static final String MEDIATYPE_IDS_NODE = "mediatypeids";

    private final JsonMapper jsonMapper;
    private final ZabbixApiService apiService;

    public ZabbixMediaTypeService(JsonMapper jsonMapper, ZabbixApiService zabbixApiService) {
        this.jsonMapper = jsonMapper;
        this.apiService = zabbixApiService;
    }

    /**
     * Get hosts request (<a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/mediatype/get">mediatype.get</a>).
     *
     * @param params The request parameter.
     * @param auth   The auth token.
     * @return A list of ZabbixGetMediaTypeDTOs.
     * @throws ZabbixApiException When the response status is not 200 or the API returned an error.
     */
    public List<ZabbixGetMediaTypeDTO> get(ZabbixGetMediaTypeParams params, String auth) throws ZabbixApiException {
        com.jit.zabbix.client.request.JsonRPCRequest request = ZabbixApiUtils.buildRequest(MediaTypeMethod.GET, params, auth);
        com.jit.zabbix.client.response.JsonRPCResponse response = apiService.call(request);
        return jsonMapper.getList(response.getResult(), ZabbixGetMediaTypeDTO.class);
    }
}
