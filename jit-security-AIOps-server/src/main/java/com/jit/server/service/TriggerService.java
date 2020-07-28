package com.jit.server.service;

import com.jit.server.request.TriggerParams;
import com.jit.server.util.StringUtils;
import com.jit.zabbix.client.dto.ZabbixTriggerDTO;
import com.jit.zabbix.client.request.ZabbixGetTriggerParams;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TriggerService {
    public List<ZabbixTriggerDTO> findByCondition(TriggerParams params) throws Exception;
    public String updateTriggerStatus(String triggerId, String status) throws Exception;
    String updateTriggerPriority(String id, String priority) throws Exception;
    public List<ZabbixTriggerDTO> findTriggerAll(TriggerParams params) throws Exception;
}
