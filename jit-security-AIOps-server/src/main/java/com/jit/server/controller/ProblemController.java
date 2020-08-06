package com.jit.server.controller;

import com.jit.server.dto.ProblemClaimDTO;
import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.request.ProblemClaimParams;
import com.jit.server.request.ProblemParams;
import com.jit.server.service.ProblemService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @PostMapping("/findByCondition")
    public Result findByCondition(@RequestBody ProblemParams params, HttpServletResponse resp) throws IOException {
        try {
            List<ZabbixProblemDTO> result = problemService.findByCondition(params);
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

    @PostMapping("/findProblemHost")
    public Result findProblemHost(@RequestBody ProblemParams params, HttpServletResponse resp) throws IOException {
        try {
            List<ProblemHostDTO> result = problemService.findProblemHost(params);
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

    @PostMapping("/findBySeverityLevel")
    public Result findBySeverityLevel(@RequestBody ProblemClaimParams params, HttpServletResponse resp) throws IOException {
        try {
            if(params != null && params.getSeverities() != null) {
                List<ProblemClaimDTO> result = problemService.findBySeverityLevel(params);
                if(null != result && !CollectionUtils.isEmpty(result)) {
                    return Result.SUCCESS(result);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addClaim")
    public Result addClaim(@RequestBody MonitorClaimEntity monitorClaimEntity) {
        try{
            if(monitorClaimEntity!=null){
                monitorClaimEntity.setClaimTime(LocalDateTime.now());
                problemService.addCalim(monitorClaimEntity);
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findClaimByUser")
    public Result findClaimByUser() {
        try{
            List<MonitorClaimEntity> list = problemService.findClaimByUser();
            return Result.SUCCESS(list);
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateClaimAfterRegister")
    public Result updateClaimAfterRegister(@RequestBody MonitorClaimEntity monitorClaimEntity) {
        try{
            problemService.updateClaimAfterRegister(monitorClaimEntity);
            return Result.SUCCESS(null);
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
