package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.CreateGraphPrototypeParams;
import com.jit.server.request.GraphPrototypeParams;
import com.jit.server.request.ItemParams;
import com.jit.server.service.GraphPrototypeService;
import com.jit.server.service.ItemService;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.request.ZabbixCreateGraphPrototypeParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: lancelot
 * @Date: 2020/07/28 10:51
 */

@RestController
@RequestMapping("/gPrototype")
public class GraphPrototypeController {
    @Autowired
    private GraphPrototypeService graphPrototypeService;

    @PostMapping("/getGProInfoList")
    public Result getGProInfoList(@RequestBody GraphPrototypeParams graphPrototypeParams, HttpServletRequest req) throws IOException {
        try {
            if (graphPrototypeParams != null) {
                List<ZabbixGetGraphPrototypeDTO> result = graphPrototypeService.getGProList(graphPrototypeParams,req);
                if (result != null && !CollectionUtils.isEmpty(result)) {
                    return Result.SUCCESS(result);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/createGpro")
    public Result createGpro(@RequestBody ZabbixCreateGraphPrototypeParams zabbixCreateGraphPrototypeParams, HttpServletRequest req) throws IOException {
        try {
            if(zabbixCreateGraphPrototypeParams != null && zabbixCreateGraphPrototypeParams.getGitems() !=null){
                List<String> graphids = graphPrototypeService.createGPro(zabbixCreateGraphPrototypeParams, req);
                if(graphids != null && !CollectionUtils.isEmpty(graphids)){
                    return Result.SUCCESS(graphids);
                }else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e){
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
