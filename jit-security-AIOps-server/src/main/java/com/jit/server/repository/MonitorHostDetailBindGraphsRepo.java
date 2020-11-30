package com.jit.server.repository;

import com.jit.server.pojo.MonitorHostDetailBindGraphs;
import com.jit.server.pojo.MonitorHostDetailBindItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: MonitorHostDetailBindGraphsRepo
 * @Author: lancelot
 * @Date: 2020.07.21
 */
@Repository
public interface MonitorHostDetailBindGraphsRepo extends JpaRepository<MonitorHostDetailBindGraphs, String>, JpaSpecificationExecutor<MonitorHostDetailBindGraphs> {


    List<MonitorHostDetailBindGraphs> findByHostIdAndIsDeletedOrderByGmtCreateDesc(String hostId, int isDeleted);

    MonitorHostDetailBindGraphs findByHostIdAndGraphIdAndIsDeleted(String hostId, String graphId, int isDeleted);
}
