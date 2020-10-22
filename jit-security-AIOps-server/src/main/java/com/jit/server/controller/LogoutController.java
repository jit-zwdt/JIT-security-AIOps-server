package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.zabbix.client.service.ZabbixUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private ZabbixUserService zabbixUserService;

    @ResponseBody
    @PostMapping(value = "/logout")
    public Result logout(HttpServletRequest req) {
        try {
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            return Result.SUCCESS(zabbixUserService.logout(auth));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

}
