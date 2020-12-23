package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.zabbix.client.service.ZabbixUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/logout")
public class LogoutController {

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private ZabbixUserService zabbixUserService;

    @ResponseBody
    @PostMapping(value = "/logout")
    @AutoLog(value = "用户登出", logType = ConstLogUtil.LOG_TYPE_LOGIN)
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
