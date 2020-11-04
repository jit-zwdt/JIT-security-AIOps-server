package com.jit.server.dto;


import com.jit.server.pojo.MonitorHostDetailBindItems;
import com.jit.zabbix.client.dto.ZabbixHistoryDTO;
import lombok.Data;

import java.util.List;

@Data
public class MonitorHostDetailBindItemsDTO {

    private MonitorHostDetailBindItems monitorHostDetailBindItems;
    private List<ZabbixHistoryDTO> zabbixHistoryDTOs;
    private String itemId;
}
