package com.jit.server.service.impl;

import com.jit.server.dto.TransferDTO;
import com.jit.server.dto.TreeNode;
import com.jit.server.pojo.SysRoleEntity;
import com.jit.server.pojo.SysRoleMenuEntity;
import com.jit.server.pojo.SysUserRoleEntity;
import com.jit.server.repository.SysMenuRepo;
import com.jit.server.repository.SysRoleMenuRepo;
import com.jit.server.repository.SysRoleRepo;
import com.jit.server.repository.SysUserRoleRepo;
import com.jit.server.service.SysRoleService;
import com.jit.server.util.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepo sysRoleRepo;

    @Autowired
    private SysUserRoleRepo sysUserRoleRepo;

    @Autowired
    private SysRoleMenuRepo sysRoleMenuRepo;

    @Autowired
    private SysMenuRepo sysMenuRepo;

    @Override
    public Page<SysRoleEntity> getRoles(PageRequest<Map<String, Object>> params) {
        Map<String, Object> param = params.getParam();
        if (param != null && !param.isEmpty()) {
            //条件
            Specification<SysRoleEntity> spec = new Specification<SysRoleEntity>() {
                @Override
                public Predicate toPredicate(Root<SysRoleEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> list = new ArrayList<Predicate>();
                    list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
                    String roleName = param.get("roleName") != null ? param.get("roleName").toString() : "";
                    if (StringUtils.isNotBlank(roleName)) {
                        list.add(cb.like(root.get("roleName").as(String.class), "%" + roleName + "%"));
                    }
                    Predicate[] arr = new Predicate[list.size()];
                    return cb.and(list.toArray(arr));
                }
            };
            //排序的定义
            List<Sort.Order> orderList = new ArrayList<>();
            orderList.add(new Sort.Order(Sort.Direction.ASC, "gmtCreate"));
            Sort sort = Sort.by(orderList);
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(params.getPage() - 1, params.getSize(), sort);
            return sysRoleRepo.findAll(spec, pageable);
        }
        return null;
    }

    @Override
    public SysRoleEntity getRoleByRoleName(String name) throws Exception {
        return sysRoleRepo.getRoleByRoleName(name);
    }

    @Override
    public SysRoleEntity getRoleByRoleSign(String sign) throws Exception {
        return sysRoleRepo.getRoleByRoleSign(sign);
    }

    @Override
    public SysRoleEntity findByIdAndIsDeleted(String id) throws Exception {
        return sysRoleRepo.findByIdAndIsDeleted(id, 0);
    }

    @Override
    public SysRoleEntity saveOrUpdateRole(SysRoleEntity sysRoleEntity) throws Exception {
        return sysRoleRepo.saveAndFlush(sysRoleEntity);
    }

    @Override
    public List<String> getRoleUsers(String roleId) throws Exception {
        List<String> res = null;
        List<Object> objectList = sysUserRoleRepo.getRoleUsers(roleId, roleId);
        if (objectList != null && !objectList.isEmpty()) {
            res = new ArrayList<>(objectList.size());
            for (Object obj : objectList) {
                Object[] arr = (Object[]) obj;
                res.add(com.jit.server.util.StringUtils.getVal(arr[0]));
            }
        }
        return res;
    }

    @Override
    public List<TransferDTO> getUsers() throws Exception {
        List<TransferDTO> res = null;
        List<Object> objectList = sysUserRoleRepo.getUsers();
        if (objectList != null && !objectList.isEmpty()) {
            res = new ArrayList<>(objectList.size());
            TransferDTO transferDTO = null;
            for (Object obj : objectList) {
                Object[] arr = (Object[]) obj;
                transferDTO = new TransferDTO();
                transferDTO.setKey(com.jit.server.util.StringUtils.getVal(arr[0]));
                transferDTO.setLabel(com.jit.server.util.StringUtils.getVal(arr[1]));
                transferDTO.setDisabled(false);
                res.add(transferDTO);
            }
        }
        return res;
    }

    @Override
    public SysUserRoleEntity getSysUserRole(String roleId, String userId) throws Exception {
        return sysUserRoleRepo.findByRoleIdAndUserIdAndIsDeleted(roleId, userId, 0);
    }

    @Override
    public List<SysUserRoleEntity> getSysUserRolesByRoleId(String roleId) throws Exception {
        return sysUserRoleRepo.findByRoleIdAndIsDeleted(roleId, 0);
    }

    @Override
    public String saveOrUpdateUserRole(SysUserRoleEntity sysUserRoleEntity) throws Exception {
        return sysUserRoleRepo.saveAndFlush(sysUserRoleEntity).getId();
    }

    @Override
    public List<String> getRoleMenus(String id) throws Exception {
        List<String> menuIds = sysRoleMenuRepo.findMenuIdByRoleId(id);
        if (menuIds != null && !menuIds.isEmpty()) {
            return Arrays.asList(menuIds.get(0).split(","));
        }
        return null;
    }

    @Override
    public List<TreeNode> getMenus() throws Exception {
        return getTreeNodeList("0");
    }

    private List<TreeNode> getTreeNodeList(String parentId) {
        List<TreeNode> nodes = new ArrayList<>();
        List<Object> nodelist = sysMenuRepo.getMenus(parentId);
        if (nodelist != null && !nodelist.isEmpty()) {
            TreeNode treeNode;
            for (Object object : nodelist) {
                treeNode = new TreeNode();
                Object[] obj = (Object[]) object;
                treeNode.setId(com.jit.server.util.StringUtils.getVal(obj[10]));
                treeNode.setLabel(com.jit.server.util.StringUtils.getVal(obj[5]));
                treeNode.setParent("0".equals(parentId) ? true : false);
                treeNode.setParentId(parentId);
                treeNode.setChildren(getTreeNodeList(com.jit.server.util.StringUtils.getVal(obj[0])));
                nodes.add(treeNode);
            }
        }
        return nodes;
    }

    @Override
    public SysRoleMenuEntity getRoleMenuByRoleId(String roleId) throws Exception {
        return sysRoleMenuRepo.findByRoleIdAndIsDeleted(roleId, 0);
    }

    @Override
    public String saveOrUpdateRoleMenu(SysRoleMenuEntity sysRoleMenuEntity) throws Exception {
        return sysRoleMenuRepo.saveAndFlush(sysRoleMenuEntity).getId();
    }

    @Override
    public List<String> getLevelOneMenuSids() throws Exception {
        return sysMenuRepo.getLevelOneMenuSids();
    }
}
