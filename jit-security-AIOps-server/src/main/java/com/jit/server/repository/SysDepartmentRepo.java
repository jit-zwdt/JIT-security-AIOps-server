package com.jit.server.repository;

import com.jit.server.pojo.SysDepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: SysDepartmentRepo
 * @Author: zengxin_miao
 * @Date: 2020.08.18
 */
@Repository
public interface SysDepartmentRepo extends JpaRepository<SysDepartmentEntity, String> {

    List<SysDepartmentEntity> findByParentIdAndIsDeletedOrderByDepartOrderAsc(String parentId, int isDeleted);

    @Query("select s.id,s.departName,s.departCode from SysDepartmentEntity s where s.parentId = ?1 and s.isDeleted = 0 order by s.departOrder")
    List<Object> getTreeNode(String parentId);
}
