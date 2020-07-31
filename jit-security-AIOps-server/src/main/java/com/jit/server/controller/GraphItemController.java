package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.GraphItemParams;
import com.jit.server.request.GraphPrototypeParams;
import com.jit.server.service.GraphItemService;
import com.jit.server.service.GraphPrototypeService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixGetGraphItemDTO;
import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.request.ZabbixCreateGraphPrototypeParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: lancelot
 * @Date: 2020/07/29 14:46
 */

@RestController
@RequestMapping("/gItem")
public class GraphItemController {
    @Autowired
    private GraphItemService graphItemService;

    @PostMapping("/getGItemInfoList")
    public Result getGProInfoList(@RequestBody GraphItemParams graphItemParams, HttpServletResponse resp) throws IOException {
        try {
            if (graphItemParams != null) {
                List<ZabbixGetGraphItemDTO> result = graphItemService.getGItemList(graphItemParams);
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
}
