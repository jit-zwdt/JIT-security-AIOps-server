package com.jit.server.controller;


import com.jit.server.exception.ExceptionEnum;
import com.jit.server.repository.SysRoleRepo;
import com.jit.server.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private SysRoleRepo sysRoleRepo;

    @PostMapping("/findAllRole")
    public Result findAllRole() {
        try{
            return Result.SUCCESS(sysRoleRepo.findAll());

        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
