package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.request.TriggerParams;
import com.jit.server.service.TriggerService;
import com.jit.server.util.PageRequest;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.model.trigger.ZabbixTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description:
 * @Author: yongbin_jiang
 * @Date: 2020/07/02 8:42
 */

@RestController
@RequestMapping("/trigger")
public class TriggerController {
    @Autowired
    TriggerService triggerService;

    @PostMapping("/findByCondition")
    public Result findByCondition(@RequestBody PageRequest<TriggerParams> params, HttpServletResponse resp) throws IOException {
        try{
            if(params!=null&&params.getParam().getHostId()!=null){
                List<ZabbixTriggerDTO> _result= triggerService.findByCondition(params.getParam());
                if (null != _result && !CollectionUtils.isEmpty(_result)) {
                    Map<String, Object> result = new HashMap<String, Object>();
                    //result.put("totalRow", 15);
                    //result.put("totalPage", 1);
                    result.put("dataList", _result);
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
    public Result updateTriggerStatus(@PathVariable String id, @RequestParam("status") String status) {
        try{
            if(StringUtils.isNotEmpty(triggerService.updateTriggerStatus(id, status))){
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

}
