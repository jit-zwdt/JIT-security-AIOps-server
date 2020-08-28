package com.jit.server.service;

import com.jit.server.pojo.SysRoleEntity;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface SysRoleService {

    Page<SysRoleEntity> getRoles(PageRequest<Map<String, Object>> params) throws Exception;

    SysRoleEntity getRoleByRoleName(String name) throws Exception;

    SysRoleEntity getRoleByRoleSign(String sign) throws Exception;

    SysRoleEntity findByIdAndIsDeleted(String id) throws Exception;

    SysRoleEntity saveOrUpdateRole(SysRoleEntity sysRoleEntity) throws Exception;

}
