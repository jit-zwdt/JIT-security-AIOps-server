package com.jit.server.service.impl;

import com.jit.server.dto.SysMenuDTO;
import com.jit.server.dto.SysMenuInfoDTO;
import com.jit.server.dto.SysMenuListDTO;
import com.jit.server.dto.SysMenuMetaDTO;
import com.jit.server.pojo.SysMenuEntity;
import com.jit.server.pojo.SysRoleEntity;
import com.jit.server.pojo.SysRoleMenuEntity;
import com.jit.server.repository.SysMenuRepo;
import com.jit.server.service.SysMenuService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String SYMBOL = "*";
    public static final String LOGIN = "/login";

    @Autowired
    private SysMenuRepo sysMenuRepo;

    @Override
    public List<SysMenuDTO> getMenus(String userid) throws Exception {
        List<SysRoleMenuEntity> list = sysMenuRepo.findSysRoleMenuEntityList(userid);
        List<String> listStr = new ArrayList<String>();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            for (SysRoleMenuEntity sr : list) {
                String menuId = sr.getMenuId();
                if (menuId != null) {
                    String[] menuSids = menuId.split(",");
                    for (String menuSid : menuSids) {
                        if (!listStr.contains(menuSid)) {
                            listStr.add(menuSid);
                            //添加 父级菜单
                            String psid = sysMenuRepo.findParentSidBySid(menuSid);
                            if (StringUtils.isNotEmpty(psid) && !listStr.contains(psid)) {
                                listStr.add(psid);
                            }
                        }
                    }
                }
            }
        }

        List<SysMenuDTO> SysMenuDTOList = getMenusByParentId(ZERO, listStr);
        SysMenuDTO sysMenuDtoLoginOut = new SysMenuDTO();
        sysMenuDtoLoginOut.setPath(SYMBOL);
        sysMenuDtoLoginOut.setRedirect(LOGIN);
        sysMenuDtoLoginOut.setIsShow(ONE);
        SysMenuDTOList.add(sysMenuDtoLoginOut);
        return SysMenuDTOList;
    }

    private List<SysMenuDTO> getMenusByParentId(String parentId, List<String> listStr) {
        List<SysMenuDTO> sysMenuDTOList = null;
        List<Object> menus = sysMenuRepo.getMenus(parentId);
        if (menus != null) {
            sysMenuDTOList = new ArrayList<>();
            for (Object obj : menus) {
                SysMenuDTO sysMenuDto = new SysMenuDTO();
                Object[] menu = (Object[]) obj;
                if (listStr.contains(StringUtils.getVal(menu[10]))) {

                    sysMenuDto.setId(StringUtils.getVal(menu[0]));
                    sysMenuDto.setPath(StringUtils.getVal(menu[1]));
                    sysMenuDto.setComponent(StringUtils.getVal(menu[2]));
                    sysMenuDto.setName(StringUtils.getVal(menu[4]));
                    sysMenuDto.setChildren(getMenusByParentId(StringUtils.getVal(menu[0]), listStr));
                    sysMenuDto.setIsRoute(StringUtils.getVal(menu[7]));
                    SysMenuMetaDTO sysMenuMetaDto = new SysMenuMetaDTO();
                    sysMenuMetaDto.setIcon(StringUtils.getVal(menu[6]));
                    sysMenuMetaDto.setTitle(StringUtils.getVal(menu[5]));
                    sysMenuDto.setMeta(sysMenuMetaDto);
                    sysMenuDto.setRedirect(StringUtils.getVal(menu[3]));
                    sysMenuDto.setIsShow(StringUtils.getVal(menu[8]));
                    sysMenuDTOList.add(sysMenuDto);
                }
            }
        }
        return sysMenuDTOList;
    }

    @Override
    public List<SysMenuListDTO> getMenusList() throws Exception {
        List<SysMenuListDTO> SysMenuDTOList = getMenusByParentIdList(ZERO);
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
        List<SysMenuListDTO> SysMenuDTOList = getMenusByParentIdFirstList(ZERO);
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
    @Transactional(rollbackFor = Exception.class)
    public void addSysMenu(SysMenuEntity sysMenuEntity) throws Exception {
        sysMenuRepo.save(sysMenuEntity);
    }

    @Override
    public SysMenuInfoDTO findSysMenuInfoBySysMenuId(String id) throws Exception {
        return sysMenuRepo.findSysMenuById(id);
    }

    @Override
    public SysMenuEntity findBySysMenuId(String id) throws Exception {
        return sysMenuRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSysMenu(SysMenuEntity sysMenuEntity) throws Exception {
        sysMenuRepo.saveAndFlush(sysMenuEntity);
    }

    /**
     * 更新 显示隐藏的方式的 业务层 如果没有则会返回空
     *
     * @param id     主键 ID
     * @param isShow 更改的 isShow 值
     * @return SysMenuEntity 对象实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysMenuEntity updateIsShow(String id, int isShow) {
        SysMenuEntity sysMenuEntity = sysMenuRepo.findById(id).get();
        if (sysMenuEntity != null) {
            sysMenuEntity.setIsShow(isShow);
            // 查看当前节点是否是 父节点 是的话 把子节点隐藏
            if (sysMenuEntity.getParentId().equals("0")) {
                List<SysMenuEntity> sysMenuEntities = sysMenuRepo.findByParentId(id);
                for (SysMenuEntity sysmenu : sysMenuEntities) {
                    sysmenu.setIsShow(isShow);
                }
            }
        }
        return sysMenuEntity;
    }

    /**
     * 根据该菜单名称是否有相同的菜单名称 如果有则返回 false 没有返回 true
     *
     * @param path    菜单路径
     * @param oldPath 旧的菜单路径
     * @return false 有 true 没有
     */
    @Override
    public Boolean getValidationName(String path, String oldPath) {
        List<SysMenuEntity> sysMenuEntities = sysMenuRepo.findByPathIsAndIsDeleted(path, ConstUtil.IS_NOT_DELETED);
        if (path.equals(oldPath)) {
            return true;
        }
        if (sysMenuEntities.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据该组件名称是否有相同的组件名称 如果有则返回 false 没有返回 true
     *
     * @param name    组件名称
     * @param oldName 旧的组件名称
     * @return false 有 true 没有
     */
    @Override
    public Boolean getValidationTitle(String name, String oldName) {
        List<SysMenuEntity> sysMenuEntities = sysMenuRepo.findByNameIsAndIsDeleted(name, ConstUtil.IS_NOT_DELETED);
        if (name.equals(oldName)) {
            return true;
        }
        if (sysMenuEntities.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据该组件路径是否有相同的组件路径 如果有则返回 false 没有返回 true
     *
     * @param component    组件路径
     * @param oldComponent 旧的组件路径
     * @return false 有 true 没有
     */
    @Override
    public Boolean getValidationComponent(String component, String oldComponent) {
        List<SysMenuEntity> sysMenuEntities = sysMenuRepo.findByComponentIsAndIsDeleted(component, ConstUtil.IS_NOT_DELETED);
        if (component.equals("Layout")) {
            return true;
        }
        if (component.equals(oldComponent)) {
            return true;
        }
        if (sysMenuEntities.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<SysMenuEntity> findByParentId(String id) {
        return sysMenuRepo.findByParentIdAndIsDel(id);
    }

    @Override
    public List<SysMenuEntity> getMenuTitle() {
        return sysMenuRepo.findByParentIdAndIsDel("0");
    }
}
