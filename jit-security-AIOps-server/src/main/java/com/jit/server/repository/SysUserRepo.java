package com.jit.server.repository;

import com.jit.server.dto.SysUserDTO;
import com.jit.server.pojo.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: zengxin_miao
 * @Date: 2020/06/09 13:25
 */
@Repository
public interface SysUserRepo extends JpaRepository<SysUserEntity, String>, JpaSpecificationExecutor<SysUserEntity> {

    @Query("SELECT u FROM SysUserEntity u WHERE u.username = ?1 and u.isDeleted = 0")
    SysUserEntity findByUsername(String username);

    @Query("SELECT u FROM SysUserEntity u WHERE u.status = 1 and u.isZabbixActive = 1 and u.username = ?1 and u.isDeleted = 0")
    SysUserEntity findZabbixActiveUserByUsername(String username);

    @Query("SELECT u FROM SysUserEntity u,SysUserRoleEntity ur WHERE u.id = ur.userId and ur.roleId = ?1 and u.isDeleted = 0 and ur.isDeleted = 0")
    List<SysUserEntity> findUserByRole(String roleId);

    @Query("SELECT u.id FROM SysUserEntity u WHERE u.status = 1 and u.username = ?1 and u.isDeleted = 0")
    String findIdByUsername(String username);

    SysUserEntity findByUsernameAndIsDeleted(String username, int isDeleted);

    @Query("SELECT u.name FROM SysUserEntity u WHERE u.id = ?1 and u.isDeleted = 0")
    String findNameById(String id);

    @Query("SELECT u.name FROM SysUserEntity u WHERE u.username = ?1 and u.isDeleted = 0")
    String findNameByUsername(String username);

    @Query(value = "SELECT new com.jit.server.dto.SysUserDTO(u.id, u.username, u.name, u.workNo, u.mobile, u.sex, u.birth, u.picId, u.picUrl, u.departmentId, u.province, u.city, u.liveAddress, u.hobby, u.email,u.status) FROM SysUserEntity u WHERE u.id = ?1 and u.isDeleted = 0")
    SysUserDTO findUserById(String id);

    SysUserEntity findByIdAndIsDeleted(String id, int isDeleted);
}
