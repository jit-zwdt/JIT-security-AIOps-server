package com.jit.server.service;

import com.jit.server.pojo.SysUserEntity;
import com.jit.server.request.SysUserEntityParams;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SysUserService {

    String findIdByUsername() throws Exception;

    Page<SysUserEntity> getUsers(PageRequest<SysUserEntityParams> params) throws Exception;

    List<SysUserEntity> findUserByRole(String roleId) throws Exception;
}
