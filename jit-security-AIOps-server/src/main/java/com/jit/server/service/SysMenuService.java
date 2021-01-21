package com.jit.server.service;

import com.jit.server.dto.SysMenuDTO;
import com.jit.server.dto.SysMenuListDTO;
import com.jit.server.pojo.SysMenuEntity;

import java.util.List;
import java.util.Optional;

public interface SysMenuService {

    List<SysMenuDTO> getMenus(String userid) throws Exception;
    List<SysMenuListDTO> getMenusList() throws Exception;
    void addSysMenu(SysMenuEntity sysMenuEntity) throws Exception;

    SysMenuEntity findBySysMenuId(String id) throws Exception;

    void updateSysMenu(SysMenuEntity sysMenuEntity) throws Exception;
    List<SysMenuListDTO> getMenusFirst() throws Exception;

    SysMenuEntity updateIsShow(String id, int isShow);

    Boolean getValidationName(String path, String oldPath);

    Boolean getValidationTitle(String title, String oldTitle);

    Boolean getValidationComponent(String component, String oldComponent);

    List<SysMenuEntity> findByParentId(String id);

    List<SysMenuEntity> getMenuTitle();
}
