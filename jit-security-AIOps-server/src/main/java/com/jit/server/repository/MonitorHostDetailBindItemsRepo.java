package com.jit.server.repository;

import com.jit.server.pojo.MonitorHostDetailBindItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: MonitorHostDetailBindItemsRepo
 * @Author: zengxin_miao
 * @Date: 2020.07.21
 */
@Repository
public interface MonitorHostDetailBindItemsRepo extends JpaRepository<MonitorHostDetailBindItems, String>, JpaSpecificationExecutor<MonitorHostDetailBindItems> {


    List<MonitorHostDetailBindItems> findByHostIdAndIsDeleted(String hostId, int isDeleted);

    MonitorHostDetailBindItems findByHostIdAndItemIdAndIsDeleted(String hostId, String itemId, int isDeleted);
}
