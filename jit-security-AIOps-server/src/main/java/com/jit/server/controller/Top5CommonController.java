package com.jit.server.controller;

import com.jit.server.config.ParamsConfig;
import com.jit.server.request.Top5Params;
import com.jit.server.service.HostService;
import com.jit.server.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zengxin_miao
 * @version 1.0
 * @date 2020.07.20
 */

@RestController
@RequestMapping("/top5")
public class Top5CommonController {

    @Autowired
    private ParamsConfig paramsConfig;

    @Autowired
    private HostService hostService;

    public static final String METHOD_NAME = "getMonitor";

    @ResponseBody
    @PostMapping(value = "/getTop5Msg")
    public Result getTop5Msg(@RequestBody Top5Params params) {
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
                            List<Map<String, String>> resList = getTop5ByItem(methodParam);
                            if (resList != null) {
                                resultList.addAll(resList);
                            }
                        }
                    }
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
    private List<Map<String, String>> getTop5ByItem(Map<String, Object> params) {
        try {
            if (params != null) {
               /* params.put("typeId", typeId);
                params.put("subtypeId", subtypeId);
                params.put("itemKey", itemKey);
                params.put("valueType", valueType);*/
                return hostService.getTop5ByItem(params);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}