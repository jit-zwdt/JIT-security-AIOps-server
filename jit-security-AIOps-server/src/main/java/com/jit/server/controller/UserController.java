package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.repository.SysUserRepo;
import com.jit.server.request.UserParams;
import com.jit.server.service.UserService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysUserRepo sysUserRepo;

    @PostMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam(value = "alias",required = false) String alias, HttpServletResponse resp){
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

    @PostMapping("/getUserAndMediaInfo")
    public Result getUserAndMediaInfo(@RequestParam(value = "alias",required = false) String alias, @RequestParam(value = "userid",required = false) String userid , HttpServletResponse resp){
        try {
            List<UserParams> result = userService.getUserAndMediaInfo(alias, userid);
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
