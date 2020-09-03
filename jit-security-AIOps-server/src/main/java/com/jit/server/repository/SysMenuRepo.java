package com.jit.server.repository;

import com.jit.server.pojo.SysMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
