package com.jit.server.service.impl;


import com.jit.server.dto.MonitorHostDetailBindItemsDTO;
import com.jit.server.pojo.MonitorHostDetailBindItems;
import com.jit.server.repository.MonitorHostDetailBindItemsRepo;
import com.jit.server.service.MonitorHostDetailBindItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @Description: MonitorHostDetailBindItemsImpl
 * @Author: zengxin_miao
 * @Date: 2020.07.21
 */
@Service
public class MonitorHostDetailBindItemsImpl implements MonitorHostDetailBindItemsService {

    @Autowired
    private MonitorHostDetailBindItemsRepo monitorHostDetailBindItemsRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MonitorHostDetailBindItems saveOrUpdateMonitorHostDetailBindItems(MonitorHostDetailBindItems monitorHostDetailBindItems) throws Exception {
        return monitorHostDetailBindItemsRepo.saveAndFlush(monitorHostDetailBindItems);
    }

    @Override
    public List<MonitorHostDetailBindItems> findMonitorHostDetailBindItemsByHostId(String hostId, int isDeleted) throws Exception {
        return monitorHostDetailBindItemsRepo.findByHostIdAndIsDeletedOrderByGmtCreateDesc(hostId, isDeleted);
    }

    @Override
    public MonitorHostDetailBindItems findById(String id) throws Exception {
        return monitorHostDetailBindItemsRepo.getOne(id);
    }

    @Override
    public MonitorHostDetailBindItems findByHostIdAndItemIdAndIsDeleted(String hostId, String itemId, int isDeleted) throws Exception {
        return monitorHostDetailBindItemsRepo.findByHostIdAndItemIdAndIsDeleted(hostId, itemId, isDeleted);
    }

    /**
     * 对DTO进行排序
     * @param list DTO集合
     * @return 集合
     */
    @Override
    public List<MonitorHostDetailBindItemsDTO> findMonitorHostDetailBindItemsDTOByHostId(List<MonitorHostDetailBindItemsDTO> list) {
        List<MonitorHostDetailBindItemsDTO> lists = null;
        if(list != null){
            lists = new ArrayList<>();
            Collections.sort(list, Comparator.comparing(a -> a.getMonitorHostDetailBindItems().getGmtCreate()));
            for (int i = list.size() - 1; i >= 0; i--) {
                MonitorHostDetailBindItemsDTO m = list.get(i);
                lists.add(m);
            }
        }
        return lists;
    }
}
