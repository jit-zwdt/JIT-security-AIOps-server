package com.jit.server.service;

import com.jit.server.request.ProblemParams;
import com.jit.server.request.TriggerConditionParams;
import com.jit.server.request.TriggerParams;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.request.ZabbixGetTriggerParams;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TriggerService {
    List<ZabbixTriggerDTO> findByCondition(TriggerParams params, HttpServletRequest req) throws Exception;
    String updateTriggerStatus(String triggerId, String status,HttpServletRequest req) throws Exception;
    String updateTriggerPriority(String id, String priority,HttpServletRequest req) throws Exception;
    List<ZabbixTriggerDTO> findTriggerAll(TriggerParams params,HttpServletRequest req) throws Exception;
}
