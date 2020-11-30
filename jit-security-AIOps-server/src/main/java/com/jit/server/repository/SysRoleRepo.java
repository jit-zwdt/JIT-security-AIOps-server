package com.jit.server.repository;

import com.jit.server.pojo.SysRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleRepo extends JpaRepository<SysRoleEntity, String>, JpaSpecificationExecutor<SysRoleEntity> {
    @Query(value = "select roleSign from SysRoleEntity where id = ?1 and isDeleted = 0")
    String findRoleSignById(String id);

    @Query("select r from SysRoleEntity r where r.isDeleted = 0 and r.roleName = ?1")
    SysRoleEntity getRoleByRoleName(String name);

    @Query("select r from SysRoleEntity r where r.isDeleted = 0 and r.roleSign = ?1")
    SysRoleEntity getRoleByRoleSign(String sign);

    SysRoleEntity findByIdAndIsDeleted(String id, int isDeleted);

    List<SysRoleEntity> findByIsDeleted(int isDeleted);
}
