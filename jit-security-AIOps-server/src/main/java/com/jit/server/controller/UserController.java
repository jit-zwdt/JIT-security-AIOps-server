package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.UserParams;
import com.jit.server.service.SysUserService;
import com.jit.server.service.UserService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @PostMapping("/getUserInfo")
    @AutoLog(value = "告警定义-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getUserInfo(@RequestParam(value = "alias", required = false) String alias, HttpServletRequest req) {
        try {
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            List<ZabbixUserDTO> result = userService.getUserInfo(alias, auth);
            if (null != result && !CollectionUtils.isEmpty(result)) {
                return Result.SUCCESS(result);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getUserAndMediaInfo")
    public Result getUserAndMediaInfo(@RequestParam(value = "alias", required = false) String alias, @RequestParam(value = "userid", required = false) String userid, HttpServletRequest req) {
        try {
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            List<UserParams> result = userService.getUserAndMediaInfo(alias, userid, auth);
            if (null != result && !CollectionUtils.isEmpty(result)) {
                return Result.SUCCESS(result);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getUserByRole")
    public Result getUserByRole(@RequestParam(value = "roleId", required = true) String roleId) {
        try {
            if (roleId != null) {
                return Result.SUCCESS(sysUserService.findUserByRole(roleId));
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateUserAndMediaInfo/{id}")
    @AutoLog(value = "告警定义-编辑", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateUserAndMediaInfo(@PathVariable String id, @RequestBody List<UserParams> tempData, HttpServletRequest req) {
        try {
            if (tempData != null) {
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                return Result.SUCCESS(userService.updateUserInfo(id, tempData, auth));
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
