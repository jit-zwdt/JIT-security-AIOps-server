package com.jit.server.service;

import com.jit.server.dto.TransferDTO;
import com.jit.server.dto.TreeNode;
import com.jit.server.pojo.SysRoleEntity;
import com.jit.server.pojo.SysRoleMenuEntity;
import com.jit.server.pojo.SysUserRoleEntity;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SysRoleService {

    Page<SysRoleEntity> getRoles(PageRequest<Map<String, Object>> params) throws Exception;

    SysRoleEntity getRoleByRoleName(String name) throws Exception;

    SysRoleEntity getRoleByRoleSign(String sign) throws Exception;

    SysRoleEntity findByIdAndIsDeleted(String id) throws Exception;

    SysRoleEntity saveOrUpdateRole(SysRoleEntity sysRoleEntity) throws Exception;

    List<String> getRoleUsers(String id) throws Exception;

    List<TransferDTO> getUsers() throws Exception;

    SysUserRoleEntity getSysUserRole(String roleId, String userId) throws Exception;

    List<SysUserRoleEntity> getSysUserRolesByRoleId(String roleId) throws Exception;

    String saveOrUpdateUserRole(SysUserRoleEntity sysUserRoleEntity) throws Exception;

    List<String> getRoleMenus(String id) throws Exception;

    List<TreeNode> getMenus() throws Exception;

    SysRoleMenuEntity getRoleMenuByRoleId(String roleId) throws Exception;

    String saveOrUpdateRoleMenu(SysRoleMenuEntity sysRoleMenuEntity) throws Exception;

    List<SysRoleEntity> findAll();

    List<SysRoleEntity> findByIsDeleted();

    List<String> getLevelOneMenuSids() throws Exception;
}
