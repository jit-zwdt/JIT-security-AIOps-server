package com.jit.server.repository;

import com.jit.server.pojo.MonitorTemplatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020-06-17 09:46:28
 */
@Repository
public interface MonitorTemplatesRepo extends JpaRepository<MonitorTemplatesEntity, String>, JpaSpecificationExecutor<MonitorTemplatesEntity> {

    List<MonitorTemplatesEntity> findByTypeIdAndIsDeletedOrderByOrderNum(String typdId, int isDeleted);

    List<MonitorTemplatesEntity> findByTypeIdAndIsDeletedAndNameLike(String typeId, int isDeleted, String keyword);
}
