package com.jit.server.repository;

import com.jit.server.pojo.SysRoleMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleMenuRepo extends JpaRepository<SysRoleMenuEntity, String>, JpaSpecificationExecutor<SysRoleMenuEntity> {
    @Query("select m.menuId from SysRoleMenuEntity m ,SysRoleEntity r where m.isDeleted = 0 and m.roleId = ?1 and m.roleId = r.id and r.isDeleted = 0")
    List<String> findMenuIdByRoleId(String id);

    @Query(value = "select rm from SysRoleMenuEntity rm ,SysRoleEntity r where r.id = rm.roleId and r.isDeleted = 0 and rm.roleId = ?1 and rm.isDeleted = ?2")
    SysRoleMenuEntity findByRoleIdAndIsDeleted(String roleId, int isDeleted);
}
