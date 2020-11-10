package com.jit.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.util.Result;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.service.ZabbixApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.06.06
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    @Autowired
    private ZabbixApiService zabbixApiService;

    @ResponseBody
    @PostMapping(value = "/getAuth")
    public Result getAuth(@RequestParam String username, @RequestParam String password) {
        Result result = null;
        String auth = "";
        JSONObject jsonObj = new JSONObject();
        StringBuffer msg = new StringBuffer(100);
        if (StringUtils.isNotBlank(username) && StringUtils.isNoneBlank(password)) {
            try {
                auth = zabbixApiService.authenticate(username, password);
                jsonObj.put("auth", auth);
                result = Result.SUCCESS(jsonObj);
            } catch (ZabbixApiException e) {
                e.printStackTrace();
                result = Result.ERROR(ExceptionEnum.TOKEN_EXCEPTION);
            }
        } else {
            if (StringUtils.isBlank(username)) {
                msg.append("username is empty ");
            }
            if (StringUtils.isBlank(password)) {
                msg.append("password is empty ");
            }
            result = Result.ERROR(ExceptionEnum.TOKEN_EXCEPTION);
            result.setMsg(msg.toString());
        }
        return result;

    }

}