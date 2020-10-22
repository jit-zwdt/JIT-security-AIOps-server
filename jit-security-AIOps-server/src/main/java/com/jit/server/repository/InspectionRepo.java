package com.jit.server.repository;

import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorSchemeTimerTaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: jian_liu
 * @Date: 2020/09/23 10:25
 */
@Repository
public interface InspectionRepo extends JpaRepository<MonitorSchemeTimerTaskEntity, String>, JpaSpecificationExecutor<MonitorSchemeTimerTaskEntity> {

    @Query("SELECT h FROM HostEntity h WHERE h.deleted = 0")
    List<HostEntity> getHostInfo();

    @Query("SELECT h FROM HostEntity h WHERE h.deleted = 0 and h.hostId = ?1")
    List<HostEntity> getHostInfoById(String id);

    /**
     * 修改数据的 is_deleted 字段变为指定的值 根据 id 修改,传入的第三个参数是父数据的 ID 如果没有就是修改一条数据如果有就是级联修改
     * @param isDelete 需要修改的值
     * @param id 数据 ID 标识
     * @param parentId 父字段 ID 的值
     */
    @Modifying
    @Query(value = "update monitor_scheme_timer_task t set t.is_deleted = ?1 where t.id = ?2 or t.parent_id = ?3" , nativeQuery = true)
    void updateIsDeleteById(int isDelete , String id , String parentId);

}
