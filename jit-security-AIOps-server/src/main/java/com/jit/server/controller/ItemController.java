package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.HostEntity;
import com.jit.server.request.ItemParams;
import com.jit.server.service.ItemService;
import com.jit.server.util.Result;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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

@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping("/getItemInfoList")
    public Result getItemInfoList(@RequestBody ItemParams itemParams, HttpServletResponse resp) throws IOException {
        try {
            if (itemParams != null && itemParams.getHostids() != null) {
                List<ZabbixGetItemDTO> result = itemService.getItemInfoList(itemParams);
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
    public Result updateItemStatus(@PathVariable String id, @RequestParam("status") String status) {
        try{
            if(StringUtils.isNotEmpty(itemService.updateItemStatus(id, status))){
                return Result.SUCCESS(null);
            }else{
                return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}