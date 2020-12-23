package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.request.*;
import com.jit.server.service.*;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lancelot
 * @Date: 2020/07/29 14:46
 */

@Slf4j
@RestController
@RequestMapping("/gItem")
public class GraphItemController {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private GraphItemService graphItemService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private TrendService trendService;
    @Autowired
    private GraphPrototypeService graphPrototypeService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;


    @PostMapping("/getGItemInfoList")
    public Result getGProInfoList(@RequestBody GraphItemParams graphItemParams, HttpServletRequest req) throws IOException {
        try {
            if (graphItemParams != null) {
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<ZabbixGetGraphItemDTO> result = graphItemService.getGItemList(graphItemParams,auth);
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
    @AutoLog(value = "监控信息-查询单个饼图", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getResultList(@RequestBody GraphItemParams graphItemParams, HttpServletRequest req) throws IOException {
        try {
            if (graphItemParams != null) {
                Map<String, Object> finalResult = new HashMap<>();
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<ZabbixGetGraphItemDTO> result = graphItemService.getGItemList(graphItemParams,auth);
                finalResult.put("gItemData", result);
                if (graphItemParams.getGraphids() != null && !CollectionUtils.isEmpty(graphItemParams.getGraphids())) {
                    GraphPrototypeParams graphPrototypeParams = new GraphPrototypeParams();
                    graphPrototypeParams.setGraphids(graphItemParams.getGraphids());
                    List<ZabbixGetGraphPrototypeDTO> graph = graphPrototypeService.getGProList(graphPrototypeParams,auth);
                    finalResult.put("graphData", graph);
                }
                List<String> itemids = new ArrayList<>();
                for (ZabbixGetGraphItemDTO z : result) {
                    itemids.add(z.getItemId());
                }
                if (itemids.size() > 0) {
                    ItemParams itemParams = new ItemParams();
                    itemParams.setItemids(itemids);
                    itemParams.setHostids(graphItemParams.getHostids());
                    List<ZabbixGetItemDTO> item = itemService.getItemInfoList(itemParams, auth);
                    for(ZabbixGetItemDTO zabbixGetItemDTO:item){
                        List<String> _itemId = new ArrayList<>();
                        HistoryParams historyParams = new HistoryParams();
                        _itemId.add(zabbixGetItemDTO.getId());
                        historyParams.setHistory(item.get(0).getValueType().getValue());
                        historyParams.setTimefrom(graphItemParams.getTimefrom());
                        historyParams.setTimetill(graphItemParams.getTimetill());
                        historyParams.setItemids(_itemId);
                        zabbixGetItemDTO.setTrend(historyService.getHistoryInfoList(historyParams, auth));
                    }
                    finalResult.put("itemData", item);
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

    @PostMapping("/getGItemByGraphId/{graphid}")
    public Result getGItemByGraphId(@PathVariable String graphid, HttpServletRequest req) throws IOException {
        try {
            GraphItemParams graphItemParams = new GraphItemParams();
            List<String> list = new ArrayList<>();
            list.add(graphid);
            graphItemParams.setGraphids(list);
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            return Result.SUCCESS(graphItemService.getGItemList(graphItemParams,auth));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    /**
     * 根据图形 ids 获取所有的图形监控项
     * @param graphids 图形 ids
     * @param req HttpServletRequest 对象
     * @return 返回统一返回响应对象
     * @throws IOException
     */
    @PostMapping("/getGItemByGraphIdAll")
    public Result getGItemByGraphId(@RequestParam("graphids[]") List<String> graphids, HttpServletRequest req) throws IOException {
        try {
            GraphItemParams graphItemParams = new GraphItemParams();
            graphItemParams.setGraphids(graphids);
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            return Result.SUCCESS(graphItemService.getGItemList(graphItemParams,auth));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }
}
