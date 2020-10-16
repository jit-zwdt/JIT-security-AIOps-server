package com.jit.server.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jit.server.service.InspectionManageService;
import com.jit.server.service.ProblemService;
import com.jit.server.service.TriggerService;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: jian_liu
 * @Date: 2020/10/12
 */
@Component("timerTask")
public class TimerTask {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private InspectionManageService inspectionManageService;

    public void taskWithParams(String param) throws Exception {
        System.out.println("这是有参示例任务：时间:" + LocalDateTime.now() + " 参数：" + param);
        if (param == null) {
            throw new Exception("参数异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(param);
        String schemeName = jsonObject.get("schemeName")+"";
        String auth = jsonObject.get("auth") + "";
        String info = jsonObject.get("info") + "";
        JSONArray infojson = JSONArray.parseArray(info);
        if (infojson == null) {
            throw new Exception("巡检对象参数异常");
        }
        List triggeridsList = new ArrayList();
        for (int i = 0; i < infojson.size(); i++) {
            JSONObject job = infojson.getJSONObject(i);
            String triggerid = job.get("triggerid") + "";
            triggeridsList.add(triggerid);
        }
        String[] triggerids = (String[]) triggeridsList.toArray(new String[]{});
        List<ZabbixProblemDTO> result = problemService.findProblemById(triggerids, auth);
        if (result == null) {
            throw new Exception("数据异常");
        }
        JSONArray resultData = new JSONArray();
        for (int i = 0; i < infojson.size(); i++) {
            JSONObject job = infojson.getJSONObject(i);
            String triggerid = job.get("triggerid") + "";
            triggeridsList.add(triggerid);
            String hostname = job.get("hostname") + "";
            String description = job.get("description") + "";
            JSONObject jsonresult = new JSONObject();
            int num = i+1;
            jsonresult.put("num",num);
            jsonresult.put("hostname",hostname);
            jsonresult.put("description",description);
            boolean checkflag = false;
            if (result.indexOf(triggerid) > -1) {
                for (int j = 0; j < result.size(); j++) {
                    ZabbixProblemDTO dto = new ZabbixProblemDTO();
                    if (triggerid.equals(dto.getObjectId())) {
                        checkflag = true;
                        jsonresult.put("datainfo", "异常！该项目安全级别为" + checkSeverity(dto.getSeverity().getValue()));
                    }
                }
            }
            if (!checkflag) {
                jsonresult.put("datainfo","正常");
                resultData.add(jsonresult);
            }
        }

        JSONObject jsonresult = new JSONObject();
        jsonresult.put("sumNum", infojson.size());
        jsonresult.put("exSumNum", result.size());
        jsonresult.put("resultData", resultData);
        jsonresult.put("schemeName", schemeName);
        inspectionManageService.createPDF(jsonresult.toString());

    }

    public void taskNoParams() {
        System.out.println("这是无参示例任务");
    }

    private String checkSeverity(int value) {
        String name = "";
        switch (value) {
            case 1:
                name = "信息";
                break;
            case 2:
                name = "警告";
                break;
            case 3:
                name = "一般严重";
                break;
            case 4:
                name = "严重";
                break;
            case 5:
                name = "灾难";
                break;
            default:
                name = "未定义";
        }
        return name;
    }
}