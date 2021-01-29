package com.jit.server.controller;


import com.jit.server.annotation.AutoLog;
import com.jit.server.dto.ProblemRegisterDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorRegisterEntity;
import com.jit.server.service.MonitorRegisterService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private MonitorRegisterService registerService;

    @PostMapping("/getRegisterByClaimIds/{id}")
    @AutoLog(value = "故障处理登记-已解决信息", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getRegisterByClaimIds(@PathVariable String id) {
        try{
            List<ProblemRegisterDTO> monitorRegisterEntity= registerService.findByClaimId(id);
            return Result.SUCCESS(monitorRegisterEntity);
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addRegister")
    @AutoLog(value = "故障处理登记-登记", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addRegister(@RequestBody MonitorRegisterEntity monitorRegisterEntity) {
        try{
            if(monitorRegisterEntity!=null){
                monitorRegisterEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
                monitorRegisterEntity.setGmtCreate(LocalDateTime.now());
                registerService.addRegister(monitorRegisterEntity);
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
