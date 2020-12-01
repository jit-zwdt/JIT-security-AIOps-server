package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.request.ProblemParams;
import com.jit.server.request.TriggerConditionParams;
import com.jit.server.request.TriggerParams;
import com.jit.server.service.TriggerService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.model.trigger.ZabbixTrigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/07/02 8:42
 */

@Slf4j
@RestController
@RequestMapping("/trigger")
public class TriggerController {
    @Autowired
    TriggerService triggerService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @PostMapping("/findByCondition")
    public Result findByCondition(@RequestBody TriggerParams params, HttpServletRequest req
    ) throws IOException {
        try{
            if(params!=null&&params.getHostId()!=null){
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<ZabbixTriggerDTO> result= triggerService.findByCondition(params, auth);
                if (null != result && !CollectionUtils.isEmpty(result)) {
                    // 过滤Zabbix中未知项
                    result = triggerService.getStateList(result);
                    return Result.SUCCESS(result);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            }else{
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
    @PutMapping("/updateTriggerStatus/{id}")
    public Result updateTriggerStatus(@PathVariable String id, @RequestParam("status") String status, HttpServletRequest req) {
        try{
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            if(StringUtils.isNotEmpty(triggerService.updateTriggerStatus(id, status, auth))){
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PutMapping("/updateTriggerPriority/{id}")
    public Result updateTriggerPriority(@PathVariable String id, @RequestParam("priority") String priority, HttpServletRequest req
    ) {
        try{
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            if(StringUtils.isNotEmpty(triggerService.updateTriggerPriority(id, priority, auth))){
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PutMapping("/findTriggerAll")
    public Result findTriggerAll(@RequestBody TriggerParams params, HttpServletRequest req) throws IOException {
        try{
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            List<ZabbixTriggerDTO> result= triggerService.findTriggerAll(params, auth);
            if (null != result && !CollectionUtils.isEmpty(result)) {
                return Result.SUCCESS(result);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
