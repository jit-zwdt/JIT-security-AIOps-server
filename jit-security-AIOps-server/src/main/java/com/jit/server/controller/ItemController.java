package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.ItemParams;
import com.jit.server.service.ItemService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
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
            if (itemParams != null) {
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
}
