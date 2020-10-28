package com.jit.server.repository;

import com.jit.server.pojo.SysCronExpressionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: SysCronExpressionRepo
 * @Author: zengxin_miao
 * @Date: 2020.10.13
 */
@Repository
public interface SysCronExpressionRepo extends JpaRepository<SysCronExpressionEntity, String>, JpaSpecificationExecutor<SysCronExpressionEntity> {

    @Query("select e.cronExpressionDesc,e.cronExpression from SysCronExpressionEntity e where e.isDeleted = 0 ")
    List<Object> getCronExpressionObject();

    /**
     * 根据表达式查询表达式
     * @param cronExpression 表达式
     * @param isDeleted 删除值 0 表示为删除 1 表示删除
     * @return 表达式对象
     */
    SysCronExpressionEntity findByCronExpressionAndIsDeleted(String cronExpression , int isDeleted);

    /**
     * 根据表达式描述查询表达式
     * @param cronExpressionDesc 表达式描述
     * @param isDeleted 删除值 0 表示为删除 1 表示删除
     * @return 表达式对象
     */
    SysCronExpressionEntity findByCronExpressionDescAndIsDeleted(String cronExpressionDesc , int isDeleted);

    /**
     * 修改 isDeleted 根据 ID
     * @param isDeleted 需要更换的值
     * @param id ID
     */
    @Modifying
    @Query("update SysCronExpressionEntity t set t.isDeleted = :isDeleted where t.id = :id")
    void updateIsDeleteByID(int isDeleted, String id);
}
