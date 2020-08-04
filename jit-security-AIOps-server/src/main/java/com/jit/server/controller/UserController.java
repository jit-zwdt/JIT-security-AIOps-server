package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.repository.SysUserRepo;
import com.jit.server.service.UserService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysUserRepo sysUserRepo;

    @PostMapping("/getUserInfo/{alias}")
    public Result getUserInfo(@PathVariable String alias) {
        try {
            List<ZabbixUserDTO> result = userService.getUserInfo(alias);
            if (null != result && !CollectionUtils.isEmpty(result)) {
                return Result.SUCCESS(result);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findUserByRole")
    public Result findUserByRole(@RequestParam(value = "roleId", required = true) Byte roleId) {
        try{
            if(roleId != null){
                return Result.SUCCESS(sysUserRepo.findUserByRole(roleId));
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
