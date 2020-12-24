package com.jit.server.repository;

import com.jit.server.pojo.SysUserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRepo extends JpaRepository<SysUserRoleEntity, String> {

    @Query(value = "select ur from SysUserRoleEntity ur ,SysRoleEntity r where ur.userId = ?1 and ur.isDeleted = ?2 and ur.roleId = r.id and r.isDeleted = 0")
    List<SysUserRoleEntity> findByUserIdAndIsDeleted(String userId, int isDeleted);

    @Query(value = "SELECT id, title FROM ( SELECT  u.id,  concat( concat( u.name, ' - ' ), d.depart_name ) AS title,  d.depart_order AS depart_order  FROM  sys_role r,  sys_user_role a,  sys_user u,  sys_department d  WHERE  r.is_deleted = 0  AND r.id = ?1  AND r.id = a.role_id  AND a.is_deleted = 0  AND u.id = a.user_id  AND u.is_deleted = 0  AND u.department_id IS NOT NULL  AND u.department_id <> ''  AND u.department_id = d.id  AND d.is_deleted = 0 UNION SELECT  u.id,  concat( u.name, ' - 未绑定部门' ) AS title,  0 AS depart_order  FROM  sys_role r,  sys_user_role a,  sys_user u  WHERE  r.is_deleted = 0  AND r.id = ?2  AND r.id = a.role_id  AND a.is_deleted = 0  AND u.id = a.user_id  AND u.is_deleted = 0  AND u.department_id = ''  OR u.department_id IS NULL  ) a ORDER BY a.depart_order", nativeQuery = true)
    List<Object> getRoleUsers(String roleId, String roleId2);

    @Query(value = "SELECT id, title FROM ( SELECT u.id AS id, concat( concat( u.name, ' - ' ), d.depart_name ) AS title, d.depart_order AS depart_order FROM sys_user u, sys_department d WHERE u.is_deleted = 0 AND u.department_id IS NOT NULL AND u.department_id <> '' AND u.department_id = d.id AND d.is_deleted = 0 UNION SELECT u.id AS id, concat( u.name, ' - 未绑定部门' ) AS title, 0 AS depart_order FROM sys_user u WHERE u.is_deleted = 0 AND u.department_id IS NULL and u.department_id = '' ) a ORDER BY a.depart_order", nativeQuery = true)
    List<Object> getUsers();

    @Query(value = "select ur from SysUserRoleEntity ur ,SysRoleEntity r where ur.roleId = ?1 and ur.userId = ?2 and ur.isDeleted = ?3 and ur.roleId = r.id and r.isDeleted = 0")
    SysUserRoleEntity findByRoleIdAndUserIdAndIsDeleted(String roleId, String userId, int isDeleted);

    @Query(value = "select ur from SysUserRoleEntity ur ,SysRoleEntity r where ur.roleId = ?1 and ur.isDeleted = ?2 and ur.roleId = r.id and r.isDeleted = 0")
    List<SysUserRoleEntity> findByRoleIdAndIsDeleted(String roleId, int i);
}
