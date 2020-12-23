package com.jit.server.controller;

import com.jit.server.annotation.AutoLog;
import com.jit.server.config.ParamsConfig;
import com.jit.server.request.Top5Params;
import com.jit.server.service.HostService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.07.20
 */

@Slf4j
@RestController
@RequestMapping("/top5")
public class Top5CommonController {

    @Autowired
    private ParamsConfig paramsConfig;

    @Autowired
    private HostService hostService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    public static final String METHOD_NAME = "getMonitor";

    @ResponseBody
    @PostMapping(value = "/getTop5Msg")
    @AutoLog(value = "监控-Top5 信息查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getTop5Msg(@RequestBody Top5Params params, HttpServletRequest req
    ) {
        Result result = null;
        String typeId = params.getTypeId();
        String subTypeId = params.getSubTypeId();
        String itemKey = params.getItemKey();
        String method = params.getMethod();
        String valueType = params.getValueType();

        Map<String, Object> methodParam = new HashMap<>();
        methodParam.put("typeId", typeId);
        methodParam.put("subtypeId", subTypeId);
        methodParam.put("valueType", valueType);

        // methodName
        String methodName = METHOD_NAME + typeId;
        // use reflect to invoke method
        try {
            Map<String, String> map = (Map<String, String>) ParamsConfig.class.getMethod(methodName).invoke(paramsConfig, new Object[]{});
            if (map != null && !map.isEmpty()) {
                String key = map.get(itemKey);
                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(method)) {
                    List<Map<String, String>> resultList = new ArrayList<>();
                    String[] itemKeys = key.split("=#=");
                    for (String k : itemKeys) {
                        methodParam.put("itemKey", k);
                        if ("top5ByItem".equals(method)) {
                            List<Map<String, String>> resList = getTop5ByItem(methodParam, req);
                            if (resList != null) {
                                resultList.addAll(resList);
                            }
                        }
                    }
                    Collections.sort(resultList, new Comparator<Map<String, String>>() {
                        @Override
                        public int compare(Map<String, String> map1, Map<String, String> map2) {
                            double a = Double.parseDouble(map1.get("value"));
                            double b = Double.parseDouble(map2.get("value"));
                            BigDecimal bigDecimala = BigDecimal.valueOf(a);
                            BigDecimal bigDecimalb = BigDecimal.valueOf(b);
                            return bigDecimalb.compareTo(bigDecimala);
                        }
                    });
                    resultList = resultList.subList(0, resultList.size() > 4 ? 5 : resultList.size());
                    return Result.SUCCESS(resultList);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * getTop5ByItem
     *
     * @param params
     * @return
     */
    private List<Map<String, String>> getTop5ByItem(Map<String, Object> params, HttpServletRequest req
    ) {
        try {
            if (params != null) {
               /* params.put("typeId", typeId);
                params.put("subtypeId", subtypeId);
                params.put("itemKey", itemKey);
                params.put("valueType", valueType);*/
                String auth = zabbixAuthService.getAuth(req.getHeader(ConstUtil.HEADER_STRING));
                return hostService.getTop5ByItem(params, auth);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
