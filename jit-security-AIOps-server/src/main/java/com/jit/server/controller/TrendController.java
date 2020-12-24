package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.dto.MonitorHostDetailBindItemsDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.MonitorHostDetailBindGraphs;
import com.jit.server.pojo.MonitorHostDetailBindItems;
import com.jit.server.request.*;
import com.jit.server.service.*;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixGetGraphItemDTO;
import com.jit.zabbix.client.dto.ZabbixGetGraphPrototypeDTO;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixHistoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: jian_liu
 * @Date: 2020/07/21 16:30
 */

@Slf4j
@RestController
@RequestMapping("/trend")
public class TrendController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private GraphItemService graphItemService;

    @Autowired
    private GraphPrototypeService graphPrototypeService;

    @Autowired
    private MonitorHostDetailBindItemsService monitorHostDetailBindItemsService;

    @Autowired
    private MonitorHostDetailBindGraphsService monitorHostDetailBindGraphsService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @PostMapping("/getItemInfoListTrends")
    @AutoLog(value = "监控信息-查询单个折线图", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getItemInfoListTrends(@RequestBody HistoryParams historyParams, HttpServletRequest req) throws IOException {
        try {
            if (historyParams != null && historyParams.getItemids() != null) {
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                List<ZabbixHistoryDTO> result = historyService.getHistoryInfoList(historyParams, auth);
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

    @PostMapping("/addHostDetailItem")
    @AutoLog(value = "监控信息-添加指标到概况", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addHostDetailItem(@RequestBody TrendParams trendParams) {
        try {
            if (trendParams != null) {
                MonitorHostDetailBindItems monitorHostDetailBindItems = new MonitorHostDetailBindItems();
                BeanUtils.copyProperties(trendParams, monitorHostDetailBindItems);
                monitorHostDetailBindItems.setGmtCreate(LocalDateTime.now());
                monitorHostDetailBindItems.setIsDeleted(ConstUtil.IS_NOT_DELETED);
                monitorHostDetailBindItems.setValueType(trendParams.getValueType());
                monitorHostDetailBindItems = monitorHostDetailBindItemsService.saveOrUpdateMonitorHostDetailBindItems(monitorHostDetailBindItems);
                if (StringUtils.isNotBlank(monitorHostDetailBindItems.getId())) {
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addHostDetailGraph")
    @AutoLog(value = "监控信息-添加图形到概况", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addHostDetailGraph(@RequestBody TrendParams trendParams) {
        try {
            if (trendParams != null) {
                MonitorHostDetailBindGraphs monitorHostDetailBindGraphs = new MonitorHostDetailBindGraphs();
                BeanUtils.copyProperties(trendParams, monitorHostDetailBindGraphs);
                monitorHostDetailBindGraphs.setGmtCreate(LocalDateTime.now());
                monitorHostDetailBindGraphs.setIsDeleted(ConstUtil.IS_NOT_DELETED);
                monitorHostDetailBindGraphs = monitorHostDetailBindGraphsService.saveOrUpdateMonitorHostDetailBindGraphs(monitorHostDetailBindGraphs);
                if (StringUtils.isNotBlank(monitorHostDetailBindGraphs.getId())) {
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }

        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteHostDetailItem/{id}")
    public Result deleteHostDetailItem(@PathVariable String id) {
        try {
            MonitorHostDetailBindItems monitorHostDetailBindItems = monitorHostDetailBindItemsService.findById(id);
            if (monitorHostDetailBindItems != null) {
                monitorHostDetailBindItems.setGmtModified(LocalDateTime.now());
                monitorHostDetailBindItems.setIsDeleted(1);
                monitorHostDetailBindItems = monitorHostDetailBindItemsService.saveOrUpdateMonitorHostDetailBindItems(monitorHostDetailBindItems);
                if (StringUtils.isNotBlank(monitorHostDetailBindItems.getId())) {
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteHostDetailGraph/{id}")
    public Result deleteHostDetailGraph(@PathVariable String id) {
        try {
            MonitorHostDetailBindGraphs monitorHostDetailBindGraphs = monitorHostDetailBindGraphsService.findById(id);
            if (monitorHostDetailBindGraphs != null) {
                monitorHostDetailBindGraphs.setGmtModified(LocalDateTime.now());
                monitorHostDetailBindGraphs.setIsDeleted(1);
                monitorHostDetailBindGraphs = monitorHostDetailBindGraphsService.saveOrUpdateMonitorHostDetailBindGraphs(monitorHostDetailBindGraphs);
                if (StringUtils.isNotBlank(monitorHostDetailBindGraphs.getId())) {
                    return Result.SUCCESS(null);
                } else {
                    return Result.ERROR(ExceptionEnum.OPERATION_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getHostDetailItems/{hostId}")
    @AutoLog(value = "监控信息-查询折线图", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getHostDetailItems(@PathVariable String hostId, @RequestBody HistoryParams historyParams, HttpServletRequest req) {
        try {
            List<MonitorHostDetailBindItemsDTO> list = new ArrayList<>();
            List<MonitorHostDetailBindItems> monitorHostDetailBindItems = monitorHostDetailBindItemsService.findMonitorHostDetailBindItemsByHostId(hostId, ConstUtil.IS_NOT_DELETED);
            List<MonitorHostDetailBindItems> list1 = new ArrayList<>();
            List<MonitorHostDetailBindItems> list2 = new ArrayList<>();
            List<MonitorHostDetailBindItems> list3 = new ArrayList<>();
            List<MonitorHostDetailBindItems> list4 = new ArrayList<>();
            List<MonitorHostDetailBindItems> list5 = new ArrayList<>();
            for(MonitorHostDetailBindItems m:monitorHostDetailBindItems){
                if(m.getValueType() == 0){
                    list1.add(m);
                }
                if(m.getValueType() == 1){
                    list2.add(m);
                }
                if(m.getValueType() == 2){
                    list3.add(m);
                }
                if(m.getValueType() == 3){
                    list4.add(m);
                }
                if(m.getValueType() == 4){
                    list5.add(m);
                }
            }

            if(list1.size()>0){
                HistoryParams type1 = new HistoryParams();
                List<String> listType1 = new ArrayList<>();
                for(MonitorHostDetailBindItems m:list1){
                    listType1.add(m.getItemId());
                }
                type1.setItemids(listType1);
                String auth1 = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                MonitorHostDetailBindItemsDTO m1 = new MonitorHostDetailBindItemsDTO();
                type1.setHistory(0);
                type1.setTimefrom(historyParams.getTimefrom());
                type1.setTimetill(historyParams.getTimetill());
                List<ZabbixHistoryDTO> result1 = historyService.getHistoryInfoList(type1, auth1);
                for(MonitorHostDetailBindItems m:list1){
                    m1 = new MonitorHostDetailBindItemsDTO();
                    m1.setItemId(m.getItemId());
                    List<ZabbixHistoryDTO> listHistory1 = new ArrayList<>();
                    m1.setMonitorHostDetailBindItems(m);
                    for(ZabbixHistoryDTO z:result1){
                        if(z.getItemId().equals(m.getItemId())){
                            listHistory1.add(z);
                        }
                    }
                    m1.setZabbixHistoryDTOs(listHistory1);
                    list.add(m1);
                }
            }
            if(list2.size()>0){
                HistoryParams type2 = new HistoryParams();
                List<String> listType2 = new ArrayList<>();
                for(MonitorHostDetailBindItems m:list2){
                    listType2.add(m.getItemId());
                }
                type2.setItemids(listType2);
                String auth2 = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                MonitorHostDetailBindItemsDTO m2 = new MonitorHostDetailBindItemsDTO();
                type2.setHistory(1);
                type2.setTimefrom(historyParams.getTimefrom());
                type2.setTimetill(historyParams.getTimetill());
                List<ZabbixHistoryDTO> result2 = historyService.getHistoryInfoList(type2, auth2);
                for(MonitorHostDetailBindItems m:list2){
                    m2 = new MonitorHostDetailBindItemsDTO();
                    m2.setItemId(m.getItemId());
                    List<ZabbixHistoryDTO> listHistory2 = new ArrayList<>();
                    m2.setMonitorHostDetailBindItems(m);
                    for(ZabbixHistoryDTO z:result2){
                        if(z.getItemId().equals(m.getItemId())){
                            listHistory2.add(z);
                        }
                    }
                    m2.setZabbixHistoryDTOs(listHistory2);
                    list.add(m2);
                }
            }
            if(list3.size()>0){
                HistoryParams type3 = new HistoryParams();
                List<String> listType3 = new ArrayList<>();
                for(MonitorHostDetailBindItems m:list3){
                    listType3.add(m.getItemId());
                }
                type3.setItemids(listType3);
                String auth3 = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                MonitorHostDetailBindItemsDTO m3 = new MonitorHostDetailBindItemsDTO();
                type3.setHistory(2);
                type3.setTimefrom(historyParams.getTimefrom());
                type3.setTimetill(historyParams.getTimetill());
                List<ZabbixHistoryDTO> result3 = historyService.getHistoryInfoList(type3, auth3);
                for(MonitorHostDetailBindItems m:list3){
                    m3 = new MonitorHostDetailBindItemsDTO();
                    m3.setItemId(m.getItemId());
                    List<ZabbixHistoryDTO> listHistory3 = new ArrayList<>();
                    m3.setMonitorHostDetailBindItems(m);
                    for(ZabbixHistoryDTO z:result3){
                        if(z.getItemId().equals(m.getItemId())){
                            listHistory3.add(z);
                        }
                    }
                    m3.setZabbixHistoryDTOs(listHistory3);
                    list.add(m3);
                }
            }
            if(list4.size()>0){
                HistoryParams type4 = new HistoryParams();
                List<String> listType4 = new ArrayList<>();
                for(MonitorHostDetailBindItems m:list4){
                    listType4.add(m.getItemId());
                }
                type4.setItemids(listType4);
                String auth4 = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                MonitorHostDetailBindItemsDTO m4 = new MonitorHostDetailBindItemsDTO();
                type4.setHistory(3);
                type4.setTimefrom(historyParams.getTimefrom());
                type4.setTimetill(historyParams.getTimetill());
                List<ZabbixHistoryDTO> result4 = historyService.getHistoryInfoList(type4, auth4);
                for(MonitorHostDetailBindItems m:list4){
                    m4 = new MonitorHostDetailBindItemsDTO();
                    m4.setItemId(m.getItemId());
                    List<ZabbixHistoryDTO> listHistory4 = new ArrayList<>();
                    for(ZabbixHistoryDTO z:result4){
                        if(z.getItemId().equals(m.getItemId())){
                            listHistory4.add(z);
                        }
                    }
                    m4.setMonitorHostDetailBindItems(m);
                    m4.setZabbixHistoryDTOs(listHistory4);
                    list.add(m4);
                }
            }
            if(list5.size()>0){
                HistoryParams type5 = new HistoryParams();
                List<String> listType5 = new ArrayList<>();
                for(MonitorHostDetailBindItems m:list5){
                    listType5.add(m.getItemId());
                }
                type5.setItemids(listType5);
                String auth5 = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                MonitorHostDetailBindItemsDTO m5 = new MonitorHostDetailBindItemsDTO();
                type5.setHistory(4);
                type5.setTimefrom(historyParams.getTimefrom());
                type5.setTimetill(historyParams.getTimetill());
                List<ZabbixHistoryDTO> result5 = historyService.getHistoryInfoList(type5, auth5);
                for(MonitorHostDetailBindItems m:list5){
                    m5 = new MonitorHostDetailBindItemsDTO();
                    m5.setItemId(m.getItemId());
                    List<ZabbixHistoryDTO> listHistory5 = new ArrayList<>();
                    for(ZabbixHistoryDTO z:result5){
                        if(z.getItemId().equals(m.getItemId())){
                            listHistory5.add(z);
                        }
                    }
                    m5.setMonitorHostDetailBindItems(m);
                    m5.setZabbixHistoryDTOs(listHistory5);
                    list.add(m5);
                }
            }
            // 将集合进行重新排序
            list = monitorHostDetailBindItemsService.findMonitorHostDetailBindItemsDTOByHostId(list);
            return Result.SUCCESS(list);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getHostDetailGraphs/{hostId}")
    public Result getHostDetailGraphs(@PathVariable String hostId, @RequestBody GraphItemParams graphItemParams, HttpServletRequest req) {
        try {
            List<Map<String, Object>> listFinal = new ArrayList<>();
            List<MonitorHostDetailBindGraphs> list = monitorHostDetailBindGraphsService.findMonitorHostDetailBindGraphsByHostId(hostId, ConstUtil.IS_NOT_DELETED);
            String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
            for(MonitorHostDetailBindGraphs m:list){
                Map<String, Object> finalResult = new HashMap<>();
                finalResult.put("id", m.getId());
                List<String> listTemp = new ArrayList<>();
                listTemp.add(m.getGraphId());
                graphItemParams.setGraphids(listTemp);
                List<ZabbixGetGraphItemDTO> result = graphItemService.getGItemList(graphItemParams,auth);
                finalResult.put("gItemData", result);
                GraphPrototypeParams graphPrototypeParams = new GraphPrototypeParams();
                graphPrototypeParams.setGraphids(graphItemParams.getGraphids());
                List<ZabbixGetGraphPrototypeDTO> graph = graphPrototypeService.getGProList(graphPrototypeParams,auth);
                finalResult.put("graphData", graph);
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
                listFinal.add(finalResult);
            }

            return Result.SUCCESS(listFinal);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }


    @PostMapping("/checkHostDetailItem")
    public Result checkHostDetailItem(@RequestBody TrendParams trendParams) {
        try {
            if (trendParams != null && StringUtils.isNotBlank(trendParams.getHostId()) && StringUtils.isNotBlank(trendParams.getItemId())) {
                String hostId = trendParams.getHostId();
                String itemId = trendParams.getItemId();
                MonitorHostDetailBindItems monitorHostDetailBindItems = monitorHostDetailBindItemsService.findByHostIdAndItemIdAndIsDeleted(hostId, itemId, ConstUtil.IS_NOT_DELETED);
                if (monitorHostDetailBindItems != null) {
                    return Result.SUCCESS(true);
                } else {
                    return Result.SUCCESS(false);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/checkHostDetailGraph")
    public Result checkHostDetailGraph(@RequestBody TrendParams trendParams) {
        try {
            if (trendParams != null && StringUtils.isNotBlank(trendParams.getHostId()) && StringUtils.isNotBlank(trendParams.getGraphId())) {
                String hostId = trendParams.getHostId();
                String graphId = trendParams.getGraphId();
                MonitorHostDetailBindGraphs monitorHostDetailBindGraphs = monitorHostDetailBindGraphsService.findByHostIdAndGraphIdAndIsDeleted(hostId, graphId, ConstUtil.IS_NOT_DELETED);
                if (monitorHostDetailBindGraphs != null) {
                    return Result.SUCCESS(true);
                } else {
                    return Result.SUCCESS(false);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

}
