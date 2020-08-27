package com.jit.server.service;

import com.jit.server.dto.SysMenuDTO;

import java.util.List;

public interface SysMenuService {

    List<SysMenuDTO> getMenus() throws Exception;
}
