package com.jit.server.repository;

import com.jit.server.pojo.SysRoleMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleMenuRepo extends JpaRepository<SysRoleMenuEntity, String>, JpaSpecificationExecutor<SysRoleMenuEntity> {
    @Query("select m.menuId from SysRoleMenuEntity m where m.isDeleted = 0 and m.roleId = ?1")
    List<String> findMenuIdByRoleId(String id);

    SysRoleMenuEntity findByRoleIdAndIsDeleted(String roleId, int isDeleted);
}
