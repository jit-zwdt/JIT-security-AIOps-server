package com.jit.server.controller;

import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.service.ZabbixApiService;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.06.06
 */


@RestController
@RequestMapping("/api")
@Api(value = "apiController call zabbix api")
public class ApiController {

    @Autowired
    private ZabbixApiService zabbixApiService;

    @ResponseBody
    @RequestMapping(value = "/getAuth", method = RequestMethod.GET)
    @ApiOperation(value = "get auth by username and password", notes = "username and password is necessary")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "username", required = true, dataType = "String", example = "Admin"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "password", required = true, dataType = "String", example = "zabbix")
    })
    public ResponseEntity<JsonResult> getAuth(@RequestParam String username, @RequestParam String password) {
        String auth = "";
        try {
            auth = zabbixApiService.authenticate(username, password);
        } catch (ZabbixApiException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = new JSONObject();
        JsonResult jsonResult = new JsonResult();
        jsonObj.put("auth", auth);
        jsonResult.setStatus("ok");
        jsonResult.setResult(jsonObj);
        return ResponseEntity.ok(jsonResult);
    }

}

class JsonResult {
    private Object result;
    private String status;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}