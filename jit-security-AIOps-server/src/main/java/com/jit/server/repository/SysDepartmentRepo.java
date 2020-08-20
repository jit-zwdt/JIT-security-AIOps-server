package com.jit.server.repository;

import com.jit.server.pojo.SysDepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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

    SysDepartmentEntity findByIdAndIsDeleted(String id, int isDeleted);

    @Query("select s.id from SysDepartmentEntity s where s.parentId = ?1 and s.isDeleted = 0")
    List<String> getSubDepIds(String parentId);

    @Transactional
    @Modifying
    @Query("update SysDepartmentEntity s set s.isDeleted = 1, s.gmtModified = ?2, s.updateBy = ?3  where s.id in ?1 and s.isDeleted <> 1")
    int delDepartmentsByIds(List<String> list, Timestamp gmtModified, String updateBy);

    SysDepartmentEntity findByDepartCodeAndIsDeleted(String departCode, int isDeleted);
}
