package com.jit.server.controller;

import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.GraphItemParams;
import com.jit.server.request.GraphPrototypeParams;
import com.jit.server.request.ItemParams;
import com.jit.server.request.TrendParams;
import com.jit.server.service.GraphItemService;
import com.jit.server.service.GraphPrototypeService;
import com.jit.server.service.ItemService;
import com.jit.server.service.TrendService;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixGetGraphItemDTO;
import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixGetTrendDTO;
import com.jit.zabbix.client.request.ZabbixCreateGraphPrototypeParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lancelot
 * @Date: 2020/07/29 14:46
 */

@RestController
@RequestMapping("/gItem")
public class GraphItemController {
    @Autowired
    private GraphItemService graphItemService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private TrendService trendService;

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

    @PostMapping("/getResultList")
    public Result getResultList(@RequestBody GraphItemParams graphItemParams, HttpServletResponse resp) throws IOException {
        try {
            if (graphItemParams != null) {
                Map<String,Object> finalResult = new HashMap<>();
                List<ZabbixGetGraphItemDTO> result = graphItemService.getGItemList(graphItemParams);
                finalResult.put("gItemData",result);
                List<String> itemids = new ArrayList<>();
                for(ZabbixGetGraphItemDTO z:result){
                    itemids.add(z.getItemId());
                }
                if(itemids != null && !CollectionUtils.isEmpty(itemids)){
                    ItemParams itemParams = new ItemParams();
                    itemParams.setItemids(itemids);
                    itemParams.setHostids(graphItemParams.getHostids());
                    List<ZabbixGetItemDTO> item = itemService.getItemInfoList(itemParams);
                    if(item != null){
                        finalResult.put("itemData",item);
                    }
                    List<List<ZabbixGetTrendDTO>> trendList = new ArrayList<>();
                    for(int i =0; i<result.size();i++){
                        TrendParams trendParams = new TrendParams();
                        List<String> _itemId = new ArrayList<>();
                        _itemId.add(result.get(i).getItemId());
                        trendParams.setItemids(_itemId);
                        trendParams.setTimefrom(graphItemParams.getTimefrom());
                        trendParams.setTimetill(graphItemParams.getTimetill());
                        List<ZabbixGetTrendDTO> trend = trendService.getTrendInfoList(trendParams);
                        trendList.add(trend);
                    }
                    if(trendList != null){
                        finalResult.put("trendListData",trendList);
                    }
                }
                if (finalResult != null) {
                    return Result.SUCCESS(finalResult);
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
