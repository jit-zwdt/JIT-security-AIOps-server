package com.jit.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.service.ZabbixApiService;
import com.jit.zabbix.client.service.ZabbixHostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/06/08 10:09
 */

@RestController
@RequestMapping("/host")
@Api(value = "HostController call zabbix api", tags = "HostController")
public class HostController {
    @Autowired
    private ZabbixApiService zabbixApiService;
    @Autowired
    private ZabbixHostService zabbixHostService;


    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    @ResponseBody
    @RequestMapping(value = "/createHost", method = RequestMethod.POST)
    @ApiOperation(value = "createHost", notes = "createHost")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", value = "authorization", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "zabbixHostDTO", value = "zabbixHostDTO", required = true, dataType = "ZabbixHostDTO"),
            @ApiImplicitParam(paramType = "query", name = "auth", value = "auth", required = true, dataType = "String")
    })
    public Result createHost(@RequestBody ZabbixHostDTO zabbixHostDTO, @RequestParam String auth) {
        try {
            String hostId = zabbixHostService.create(zabbixHostDTO, auth);
        } catch (ZabbixApiException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = new JSONObject();

        return Result.SUCCESS(jsonObj);
    }
}
