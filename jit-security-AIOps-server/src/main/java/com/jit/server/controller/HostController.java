package com.jit.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixHostDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.service.ZabbixApiService;
import com.jit.zabbix.client.service.ZabbixHostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/06/08 10:09
 */

@Api(tags = "HostController")
@RestController
@RequestMapping("/host")
public class HostController {
    @Autowired
    private ZabbixApiService zabbixApiService;
    @Autowired
    private ZabbixHostService zabbixHostService;

    @ApiOperation(value = "createHost", notes = "createHost notes")
    @ResponseBody
    @PostMapping(value = "/createHost")
    public Result createHost(@RequestHeader String authorization, @RequestBody ZabbixHostDTO zabbixHostDTO, @RequestParam String auth) {
        try {
            String hostId = zabbixHostService.create(zabbixHostDTO, auth);
        } catch (ZabbixApiException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = new JSONObject();

        return Result.SUCCESS(jsonObj);
    }
}
