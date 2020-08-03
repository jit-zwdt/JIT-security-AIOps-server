package com.jit.server.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jit.server.config.ParamsConfig;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.service.HomePageService;
import com.jit.server.service.HostService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import com.jit.zabbix.client.dto.ZabbixGetItemDTO;
import com.jit.zabbix.client.dto.ZabbixHistoryDTO;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.exception.ZabbixApiException;
import com.jit.zabbix.client.model.problem.ProblemSeverity;
import com.jit.zabbix.client.request.ZabbixGetHistoryParams;
import com.jit.zabbix.client.request.ZabbixGetItemParams;
import com.jit.zabbix.client.request.ZabbixGetProblemParams;
import com.jit.zabbix.client.service.ZabbixHistoryService;
import com.jit.zabbix.client.service.ZabbixItemService;
import com.jit.zabbix.client.service.ZabbixProblemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/07/30 15:46
 */
@RestController
@RequestMapping("/homePage")
public class HomePageController {

    @Autowired
    private HomePageService homePageService;

    @Autowired
    private ZabbixProblemService zabbixProblemService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private ParamsConfig paramsConfig;

    @Autowired
    private HostService hostService;

    @Autowired
    private ZabbixItemService zabbixItemService;

    @Autowired
    private ZabbixHistoryService zabbixHistoryService;


    @ResponseBody
    @PostMapping(value = "/getMonitorTypeUsedInfo")
    public Result getMonitorTypeUsedInfo(HttpServletRequest req) {
        try {
            List<Object> objectList = homePageService.getHostByType();
            JSONArray jsonArray = new JSONArray();
            if (objectList != null && !objectList.isEmpty()) {
                String hostId = "";
                String type = "";
                String typeId = "";
                List<String> types = new ArrayList<>();
                List<String> typeIds = new ArrayList<>();
                List<String> hostIds = null;
                JSONObject jsonObject;
                for (int i = 0, len = objectList.size(); i < len; i++) {
                    Object[] objs = (Object[]) objectList.get(i);
                    hostId = objs[0].toString();
                    type = objs[1].toString();
                    typeId = objs[2].toString();
                    if (typeIds.contains(typeId)) {
                        hostIds.add(hostId);
                    } else {
                        types.add(type);
                        typeIds.add(typeId);
                        if (i > 0) {//exclude hostIds null
                            jsonObject = new JSONObject(5);
                            jsonObject.put("type", types.get(typeIds.indexOf(typeId) - 1));
                            jsonObject.put("typeId", typeIds.get(typeIds.indexOf(typeId) - 1));
                            jsonObject.put("hostIds", hostIds);
                            jsonObject.put("hostCount", hostIds.size());
                            jsonObject.put("problemsSeverityCount", countProblemsGroupBySeverity(hostIds));
                            jsonArray.add(jsonObject);
                        }
                        hostIds = new ArrayList<>();
                        hostIds.add(hostId);
                    }
                }
                if (!hostIds.isEmpty()) {// the last param
                    jsonObject = new JSONObject();
                    jsonObject.put("type", type);
                    jsonObject.put("typeId", typeId);
                    jsonObject.put("hostIds", hostIds);
                    jsonObject.put("hostCount", hostIds.size());
                    jsonObject.put("problemsSeverityCount", countProblemsGroupBySeverity(hostIds));
                    jsonArray.add(jsonObject);
                }
            }
            return Result.SUCCESS(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    /**
     * count problem group by severity
     *
     * @param hostIds
     * @return
     * @throws Exception
     */
    private Map<String, Integer> countProblemsGroupBySeverity(List<String> hostIds) throws Exception {
        Map<String, Integer> res = new HashMap<>();
        res.put("notClassifiedCount", 0);
        res.put("informationCount", 0);
        res.put("warningCount", 0);
        res.put("averageCount", 0);
        res.put("highCount", 0);
        res.put("disasterCount", 0);
        if (hostIds != null && !hostIds.isEmpty()) {
            String auth = zabbixAuthService.getAuth();
            ZabbixGetProblemParams zabbixGetProblemParams = new ZabbixGetProblemParams();
            zabbixGetProblemParams.setHostids(hostIds);
            zabbixGetProblemParams.setOutput("extend");
            List<ZabbixProblemDTO> zabbixProblemDTOList = zabbixProblemService.get(zabbixGetProblemParams, auth);
            if (zabbixProblemDTOList != null && !zabbixProblemDTOList.isEmpty()) {
                int notClassifiedCount = 0;
                int informationCount = 0;
                int warningCount = 0;
                int averageCount = 0;
                int highCount = 0;
                int disasterCount = 0;
                for (ZabbixProblemDTO zabbixProblemDTO : zabbixProblemDTOList) {
                    if (ProblemSeverity.NOT_CLASSIFIED.equals(zabbixProblemDTO.getSeverity())) {
                        ++notClassifiedCount;
                    }
                    if (ProblemSeverity.INFORMATION.equals(zabbixProblemDTO.getSeverity())) {
                        ++informationCount;
                    }
                    if (ProblemSeverity.WARNING.equals(zabbixProblemDTO.getSeverity())) {
                        ++warningCount;
                    }
                    if (ProblemSeverity.AVERAGE.equals(zabbixProblemDTO.getSeverity())) {
                        ++averageCount;
                    }
                    if (ProblemSeverity.HIGH.equals(zabbixProblemDTO.getSeverity())) {
                        ++highCount;
                    }
                    if (ProblemSeverity.DISASTER.equals(zabbixProblemDTO.getSeverity())) {
                        ++disasterCount;
                    }
                }
                res.put("notClassifiedCount", notClassifiedCount);
                res.put("informationCount", informationCount);
                res.put("warningCount", warningCount);
                res.put("averageCount", averageCount);
                res.put("highCount", highCount);
                res.put("disasterCount", disasterCount);
            }
            return res;
        } else {
            return res;
        }
    }

    @ResponseBody
    @PostMapping(value = "/getTimeTop5ItemInfo/{item}")
    public Result getTimeTop5ItemInfo(@PathVariable String item) {
        try {
            if (StringUtils.isNotBlank(item)) {
                //items key
                String key = paramsConfig.getItem().get(item);
                if (StringUtils.isNotBlank(key)) {
                    String auth = zabbixAuthService.getAuth();
                    List<Object> hosts = hostService.getHostIds();
                    List<String> hostIds = new ArrayList<>(hosts != null ? hosts.size() : 1);
                    Map<String, String> hostNameMap = new HashMap<>(hosts != null ? hosts.size() : 1);
                    for (Object o : hosts) {
                        Object[] arr = (Object[]) o;
                        hostIds.add(arr[0].toString());
                        hostNameMap.put(arr[0].toString(), arr[1].toString());
                    }
                    List<ItemC> itemCList = new ArrayList<>(hostIds.size());
                    List<ItemC> tempItemCList;
                    ItemC itemC;
                    for (String hostId : hostIds) {
                        itemC = getItemLastvalue(hostId, key, auth);
                        if (itemC != null) {
                            itemC.setHostName(hostNameMap.get(hostId));
                            itemCList.add(itemC);
                        }
                    }
                    //sort by value desc
                    Collections.sort(itemCList, Comparator.comparing(ItemC::getValue).reversed());
                    //get top5 hostid
                    if (itemCList.size() > 5) {
                        tempItemCList = itemCList.subList(0, 5);
                    } else {
                        tempItemCList = itemCList;
                    }
                    ChartData chartData = getHistoryItemvalue24H(tempItemCList, auth);
                    return Result.SUCCESS(chartData);
                } else {
                    return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
                }
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    private ItemC getItemLastvalue(String hostId, String key, String auth) throws Exception {
        ItemC res = null;
        String[] keys = key.split(ConstUtil.PROP_DIVIDER);
        ZabbixGetItemParams zabbixGetItemParams = new ZabbixGetItemParams();
        List<String> hostIds = new ArrayList<>(1);
        hostIds.add(hostId);
        Map<String, Object> searchMap = new HashMap<>(1);
        for (String k : keys) {
            searchMap.put("key_", k);
            zabbixGetItemParams.setHostIds(hostIds);
            zabbixGetItemParams.setSearch(searchMap);
            List<ZabbixGetItemDTO> zabbixGetItemDTOList = zabbixItemService.get(zabbixGetItemParams, auth);
            if (zabbixGetItemDTOList.size() > 0) {
                res = new ItemC();
                res.setHostId(hostId);
                res.setItemId(zabbixGetItemDTOList.get(0).getId());
                res.setValue(zabbixGetItemDTOList.get(0).getLastvalue());
                break;
            }
        }
        return res;
    }

    private ChartData getHistoryItemvalue24H(List<ItemC> itemCList, String auth) throws ZabbixApiException {
        List<String> getHours = getHours();
        ChartData chartData = new ChartData();
        chartData.setxAxis(getHours);
        List<Map<String, Object>> series = new ArrayList<>();
        for (ItemC itemC : itemCList) {
            series.add(getHourAvgVal(getHours, itemC, auth));
        }
        chartData.setSeries(series);
        return chartData;
    }

    private List<String> getHours() {
        List<String> hours = new ArrayList<>(24);
        LocalDateTime currentTime = LocalDateTime.now();
        int now = currentTime.getHour();
        for (int i = 0; i <= now; i++) {
            if (String.valueOf(i).length() < 2) {
                hours.add("0" + i);
            } else {
                hours.add("" + i);
            }
        }
        return hours;
    }

    private Map<String, Object> getHourAvgVal(List<String> hours, ItemC itemC, String auth) throws ZabbixApiException {
        Map<String, Object> res = new HashMap<>();

        List<String> millis = new ArrayList<>(24);
        for (String hour : hours) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            millis.add(getMillis(calendar.getTime()));
        }

        List<String> values = new ArrayList<>();
        // init 0 point value
        values.add("0");
        for (int m = 1, len = millis.size(); m < len; m++) {
            values.add(getTimelastvalue(millis.get(m - 1), millis.get(m), itemC, auth));
        }
        res.put("name", itemC.getHostName());
        res.put("data", values);
        return res;
    }

    private String getTimelastvalue(String timeFrom, String timeTill, ItemC itemC, String auth) throws ZabbixApiException {
        String res = "0";
        ZabbixGetHistoryParams zabbixGetHistoryParams = new ZabbixGetHistoryParams();
        zabbixGetHistoryParams.setTimeFrom(timeFrom);
        zabbixGetHistoryParams.setTimeTill(timeTill);
        List<String> hostIds = new ArrayList<>();
        hostIds.add(itemC.getHostId());
        zabbixGetHistoryParams.setHostIds(hostIds);
        List<String> itemIds = new ArrayList<>();
        itemIds.add(itemC.getItemId());
        zabbixGetHistoryParams.setItemIds(itemIds);
        zabbixGetHistoryParams.setLimit(ConstUtil.LIMIT_MAX);
        List<ZabbixHistoryDTO> zabbixHistoryDTOList = zabbixHistoryService.get(zabbixGetHistoryParams, auth);
        if (zabbixHistoryDTOList != null && !zabbixHistoryDTOList.isEmpty()) {
            BigDecimal total = new BigDecimal("0");
            for (ZabbixHistoryDTO zabbixHistoryDTO : zabbixHistoryDTOList) {
                total = total.add(new BigDecimal(zabbixHistoryDTO.getValue()));
            }
            res = (total.divide(new BigDecimal(zabbixHistoryDTOList.size()), 4, BigDecimal.ROUND_HALF_UP)).toString();
        }
        return res;
    }


    private String getMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.getTimeInMillis() / 1000);
    }

    public class ChartData {
        private List<String> xAxis;
        private List<Map<String, Object>> series;

        public List<String> getxAxis() {
            return xAxis;
        }

        public void setxAxis(List<String> xAxis) {
            this.xAxis = xAxis;
        }

        public List<Map<String, Object>> getSeries() {
            return series;
        }

        public void setSeries(List<Map<String, Object>> series) {
            this.series = series;
        }
    }


    public class ItemC {
        private String hostId;
        private String hostName;
        private String itemId;
        private String value;

        public String getHostId() {
            return hostId;
        }

        public void setHostId(String hostId) {
            this.hostId = hostId;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }
    }
}
