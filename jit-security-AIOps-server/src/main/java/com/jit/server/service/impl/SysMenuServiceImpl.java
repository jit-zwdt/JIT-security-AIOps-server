package com.jit.server.service.impl;

import com.jit.server.dto.SysMenuDTO;
import com.jit.server.dto.SysMenuListDTO;
import com.jit.server.dto.SysMenuMetaDTO;
import com.jit.server.pojo.SysMenuEntity;
import com.jit.server.repository.SysMenuRepo;
import com.jit.server.service.SysMenuService;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    public static final String ONE = "1";
    public static final String SYMBOL = "*";
    public static final String LOGIN = "/login";

    @Autowired
    private SysMenuRepo sysMenuRepo;

    @Override
    public List<SysMenuDTO> getMenus() throws Exception {
        List<SysMenuDTO> SysMenuDTOList = getMenusByParentId("0");
        SysMenuDTO sysMenuDtoLoginOut = new SysMenuDTO();
        sysMenuDtoLoginOut.setPath(SYMBOL);
        sysMenuDtoLoginOut.setRedirect(LOGIN);
        sysMenuDtoLoginOut.setIsShow(ONE);
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
                sysMenuDto.setIsShow(StringUtils.getVal(menu[8]));
                sysMenuDTOList.add(sysMenuDto);
            }
        }
        return sysMenuDTOList;
    }
    @Override
    public List<SysMenuListDTO> getMenusList() throws Exception {
        List<SysMenuListDTO> SysMenuDTOList = getMenusByParentIdList("0");
        return SysMenuDTOList;
    }

    private List<SysMenuListDTO> getMenusByParentIdList(String parentId) {
        List<SysMenuListDTO> sysMenuDTOList = null;
        List<Object> menus = sysMenuRepo.getMenus(parentId);
        if (menus != null) {
            sysMenuDTOList = new ArrayList<>();
            for (Object obj : menus) {
                SysMenuListDTO sysMenuDto = new SysMenuListDTO();
                Object[] menu = (Object[]) obj;
                sysMenuDto.setId(StringUtils.getVal(menu[0]));
                sysMenuDto.setPath(StringUtils.getVal(menu[1]));
                sysMenuDto.setComponent(StringUtils.getVal(menu[2]));
                sysMenuDto.setName(StringUtils.getVal(menu[4]));
                sysMenuDto.setChildren(getMenusByParentIdList(StringUtils.getVal(menu[0])));
                sysMenuDto.setIcon(StringUtils.getVal(menu[6]));
                sysMenuDto.setTitle(StringUtils.getVal(menu[5]));
                sysMenuDto.setIsRoute(StringUtils.getVal(menu[7]));
                sysMenuDto.setRedirect(StringUtils.getVal(menu[3]));
                sysMenuDto.setIsShow(StringUtils.getVal(menu[8]));
                sysMenuDto.setOrderNum(StringUtils.getVal(menu[9]));
                sysMenuDTOList.add(sysMenuDto);
            }
        }
        return sysMenuDTOList;
    }

    @Override
    public List<SysMenuListDTO> getMenusFirst() throws Exception {
        List<SysMenuListDTO> SysMenuDTOList = getMenusByParentIdFirstList("0");
        return SysMenuDTOList;
    }

    private List<SysMenuListDTO> getMenusByParentIdFirstList(String parentId) {
        List<SysMenuListDTO> sysMenuDTOList = null;
        List<Object> menus = sysMenuRepo.getMenus(parentId);
        if (menus != null) {
            sysMenuDTOList = new ArrayList<>();
            for (Object obj : menus) {
                SysMenuListDTO sysMenuDto = new SysMenuListDTO();
                Object[] menu = (Object[]) obj;
                sysMenuDto.setId(StringUtils.getVal(menu[0]));
                sysMenuDto.setPath(StringUtils.getVal(menu[1]));
                sysMenuDto.setComponent(StringUtils.getVal(menu[2]));
                sysMenuDto.setName(StringUtils.getVal(menu[4]));
                sysMenuDto.setIcon(StringUtils.getVal(menu[6]));
                sysMenuDto.setTitle(StringUtils.getVal(menu[5]));
                sysMenuDto.setIsRoute(StringUtils.getVal(menu[7]));
                sysMenuDto.setRedirect(StringUtils.getVal(menu[3]));
                sysMenuDto.setIsShow(StringUtils.getVal(menu[8]));
                sysMenuDto.setOrderNum(StringUtils.getVal(menu[9]));
                sysMenuDTOList.add(sysMenuDto);
            }
        }
        return sysMenuDTOList;
    }

    @Override
    public void addSysMenu(SysMenuEntity sysMenuEntity) throws Exception {
        sysMenuRepo.save(sysMenuEntity);
    }

    @Override
    public Optional<SysMenuEntity> findBySysMenuId(String id) throws Exception {
        return sysMenuRepo.findById(id);
    }

    @Override
    public void updateSysMenu(SysMenuEntity sysMenuEntity) throws Exception {
        sysMenuRepo.save(sysMenuEntity);
    }
}
