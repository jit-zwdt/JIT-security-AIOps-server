package com.jit.server.repository;

import com.jit.server.pojo.SysMenuEntity;
import com.jit.server.pojo.SysRoleMenuEntity;
import com.jit.server.pojo.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Description: SysMenuRepo
 * @Author: zengxin_miao
 * @Date: 2020.08.26
 */
@Repository
public interface SysMenuRepo extends JpaRepository<SysMenuEntity, String>, JpaSpecificationExecutor<SysMenuEntity> {

    /**
     * Get Menus
     *
     * @return Object List
     */
    @Query("SELECT u.id,u.path,u.component,u.redirect,u.name,u.title,u.icon,u.isRoute,u.isShow,u.orderNum,u.sid FROM SysMenuEntity u WHERE  u.isDeleted = 0 and u.parentId = ?1 order by u.orderNum")
    List<Object> getMenus(String parentId);

    @Query("SELECT srm FROM SysUserRoleEntity ur, SysRoleEntity sr, SysRoleMenuEntity srm WHERE sr.id = ur.roleId and sr.isDeleted = 0 and sr.id = srm.roleId and srm.isDeleted = 0 and ur.userId = ?1 and ur.isDeleted = 0")
    List<SysRoleMenuEntity> findSysRoleMenuEntityList(String id);

    Optional<SysMenuEntity> findByIdAndIsDeleted(String id, int isDeleted);
}
