package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.request.ItemParams;
import com.jit.server.service.ItemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: jian_liu
 * @Date: 2020/07/01 10:57
 */

@Slf4j
@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @PostMapping("/getItemInfoList")
    public Result getItemInfoList(@RequestBody ItemParams itemParams, HttpServletRequest req) throws IOException {
        try {
            if (itemParams != null && itemParams.getHostids() != null) {
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<ZabbixGetItemDTO> result = itemService.getItemInfoList(itemParams,auth);
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
    @PutMapping("/updateItemStatus/{id}")
    public Result updateItemStatus(@PathVariable String id, @RequestParam("status") String status, HttpServletRequest req) {
        try{
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            if(StringUtils.isNotEmpty(itemService.updateItemStatus(id, status,auth))){
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
