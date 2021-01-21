package com.jit.server.service;

import com.jit.server.dto.SysUserDTO;
import com.jit.server.pojo.SysUserEntity;
import com.jit.server.request.SysUserEntityParams;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SysUserService {

    String findIdByUsername() throws Exception;

    Page<SysUserDTO> getUsers(PageRequest<SysUserEntityParams> params) throws Exception;

    List<SysUserEntity> findUserByRole(String roleId) throws Exception;

    void addUser(SysUserEntity params) throws Exception;

    void updateUser(SysUserDTO params) throws Exception;

    void updatePassword(SysUserEntity params) throws Exception;

    SysUserEntity getByUserName(String username) throws Exception;

    SysUserEntity findById(String id) throws Exception;

    SysUserDTO findUserById(String id) throws Exception;

    void deleteUser(String id) throws Exception;
}
