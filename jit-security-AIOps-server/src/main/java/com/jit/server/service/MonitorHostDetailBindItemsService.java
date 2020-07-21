package com.jit.server.service;

import com.jit.server.pojo.MonitorHostDetailBindItems;

import java.util.List;

/**
 * @Description: MonitorHostDetailBindItemsService
 * @Author: zengxin_miao
 * @Date: 2020.07.21
 */
public interface MonitorHostDetailBindItemsService {

    void saveOrUpdateMonitorHostDetailBindItems(MonitorHostDetailBindItems monitorHostDetailBindItems) throws Exception;

    List<MonitorHostDetailBindItems> findMonitorHostDetailBindItemsByHostId(String hostId, int isDeleted) throws Exception;
}
