package com.jit.server.repository;

import com.jit.server.dto.SysMenuInfoDTO;
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

    @Query("SELECT new com.jit.server.dto.SysMenuInfoDTO(u.id, u.sid, u.parentId, u.name, u.title, u.path, u.component, u.icon, u.menuType, u.isShow, u.orderNum, u.redirect, u.isRoute, u.hiddenRoute) FROM SysMenuEntity u WHERE  u.isDeleted = 0 and u.id = ?1")
    SysMenuInfoDTO findSysMenuById(String id);

    List<SysMenuEntity> findByParentId(String parentid);

    List<SysMenuEntity> findByPathIsAndIsDeleted(String path, int i);

    List<SysMenuEntity> findByNameIsAndIsDeleted(String title, int i);

    List<SysMenuEntity> findByComponentIsAndIsDeleted(String component, int i);

    @Query(value = "select * from sys_menu where parent_id = ?1 and is_deleted = 0", nativeQuery = true)
    List<SysMenuEntity> findByParentIdAndIsDel(String id);

    @Query(value = "select m.sid from sys_menu m where m.id = (select m1.parent_id from sys_menu m1 where m1.is_deleted = 0 and m1.sid = ?1 ) and m.is_deleted = 0", nativeQuery = true)
    String findParentSidBySid(String menuSid);

    @Query("SELECT u.sid FROM SysMenuEntity u WHERE  u.isDeleted = 0 and u.parentId = '0' order by u.orderNum")
    List<String> getLevelOneMenuSids();

    SysMenuEntity findByIdAndIsDeleted(String id, int isNotDeleted);
}
