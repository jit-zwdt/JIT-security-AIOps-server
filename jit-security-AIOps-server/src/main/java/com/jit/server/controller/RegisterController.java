package com.jit.server.controller;


import com.jit.server.dto.ProblemRegisterDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorRegisterEntity;
import com.jit.server.service.MonitorRegisterService;
import com.jit.server.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private MonitorRegisterService registerService;

    @PostMapping("/findRegisterByClaimId/{id}")
    public Result findRegisterByClaimId(@PathVariable String id) {
        try{
            List<ProblemRegisterDTO> monitorRegisterEntity= registerService.findByClaimId(id);
            return Result.SUCCESS(monitorRegisterEntity);
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addRegister")
    public Result addRegister(@RequestBody MonitorRegisterEntity monitorRegisterEntity) {
        try{
            if(monitorRegisterEntity!=null){
                return Result.SUCCESS(registerService.addRegister(monitorRegisterEntity));
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
