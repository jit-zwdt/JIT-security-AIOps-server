package com.jit.server.service.impl;

import com.jit.server.dto.SysMenuDTO;
import com.jit.server.dto.SysMenuMetaDTO;
import com.jit.server.repository.SysMenuRepo;
import com.jit.server.service.SysMenuService;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    public static final String ONE = "1";
    public static final String SYMBOL1 = "*";
    public static final String LOGIN = "/login";

    @Autowired
    private SysMenuRepo sysMenuRepo;

    @Override
    public List<SysMenuDTO> getMenus() throws Exception {
        List<SysMenuDTO> SysMenuDTOList = getMenusByParentId("0");
        SysMenuDTO sysMenuDtoLoginOut = new SysMenuDTO();
        sysMenuDtoLoginOut.setPath(SYMBOL1);
        sysMenuDtoLoginOut.setRedirect(LOGIN);
        SysMenuDTOList.add(sysMenuDtoLoginOut);
        return SysMenuDTOList;
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
                sysMenuDto.setName(StringUtils.getVal(menu[4]));
                sysMenuDto.setChildren(getMenusByParentId(StringUtils.getVal(menu[0])));
                String isroute = StringUtils.getVal(menu[7]);
                if (ONE.equals(isroute)) {
                    SysMenuMetaDTO sysMenuMetaDto = new SysMenuMetaDTO();
                    sysMenuMetaDto.setIcon(StringUtils.getVal(menu[6]));
                    sysMenuMetaDto.setTitle(StringUtils.getVal(menu[5]));
                    sysMenuDto.setMeta(sysMenuMetaDto);
                }
                sysMenuDto.setRedirect(StringUtils.getVal(menu[3]));
                sysMenuDTOList.add(sysMenuDto);
            }
        }
        return sysMenuDTOList;
    }
}
