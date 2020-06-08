package com.jit.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.util.JsonResult;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.service.ZabbixApiService;
import com.jit.zabbix.client.service.ZabbixHostService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/06/08 10:09
 */
public class HostController {
    @Autowired
    private ZabbixApiService zabbixApiService;
    @Autowired
    private ZabbixHostService zabbixHostService;

    @ResponseBody
    @RequestMapping(value = "/createHost", method = RequestMethod.POST)
    @ApiOperation(value = "get auth by username and password", notes = "username and password is necessary")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", value = "authorization", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "params", value = "params", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "auth", value = "auth", required = true, dataType = "String")
    })
    public ResponseEntity<JsonResult> createHost(@RequestParam String params, @RequestParam String auth) {
        //zabbixHostService.create();
        JSONObject jsonObj = new JSONObject();
        JsonResult jsonResult = new JsonResult();
        jsonObj.put("auth", auth);
        jsonResult.setStatus("ok");
        jsonResult.setResult(jsonObj);
        return ResponseEntity.ok(jsonResult);
    }
}
