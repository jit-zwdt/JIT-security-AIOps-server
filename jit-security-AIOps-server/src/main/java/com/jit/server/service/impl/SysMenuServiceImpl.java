package com.jit.server.service.impl;

import com.jit.server.dto.SysMenuDTO;
import com.jit.server.repository.SysMenuRepo;
import com.jit.server.service.SysMenuService;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuRepo sysMenuRepo;

    @Override
    public List<SysMenuDTO> getMenus() throws Exception {
        return getMenusByParentId("0");
    }

    private List<SysMenuDTO> getMenusByParentId(String parentId) {
        List<SysMenuDTO> sysMenuDTOList = null;
        List<Object> menus = sysMenuRepo.getMenus(parentId);
        if (menus != null) {
            sysMenuDTOList = new ArrayList<>();
            for (Object obj : menus) {
                SysMenuDTO sysMenuDto = new SysMenuDTO();
                Object[] menu = (Object[]) obj;
                sysMenuDto.setId(StringUtils.getVal(menu[0]));
                sysMenuDto.setPath(StringUtils.getVal(menu[1]));
                sysMenuDto.setComponent(StringUtils.getVal(menu[2]));
                sysMenuDto.setRedirect(StringUtils.getVal(menu[3]));
                sysMenuDto.setName(StringUtils.getVal(menu[4]));
                sysMenuDto.setTitle(StringUtils.getVal(menu[5]));
                sysMenuDto.setIcon(StringUtils.getVal(menu[6]));
                sysMenuDto.setIsShow(StringUtils.getVal(menu[7]));
                sysMenuDto.setChildren(getMenusByParentId(StringUtils.getVal(menu[0])));
                sysMenuDTOList.add(sysMenuDto);
            }
        }
        return sysMenuDTOList;
    }
}
