package com.jit.server.service.impl;

import com.jit.server.dto.SysRoleDTO;
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
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<SysRoleDTO> getRoles(PageRequest<Map<String, Object>> params) {
        Map<String, Object> param = params.getParam();
        if (param != null && !param.isEmpty()) {
            int size = params.getSize();
            int page = params.getPage() - 1;
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SysRoleDTO> query = cb.createQuery(SysRoleDTO.class);
            Root<SysRoleEntity> root = query.from(SysRoleEntity.class);
            Path<String> id = root.get("id");
            Path<String> roleName = root.get("roleName");
            Path<String> roleSign = root.get("roleSign");
            Path<String> remark = root.get("remark");
            Path<LocalDateTime> gmtCreate = root.get("gmtCreate");
            //查询字段
            query.multiselect(id, roleName, roleSign, remark);
            //查询条件
            List<Predicate> list = new ArrayList<>();
            list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
            String name = param.get("roleName") != null ? param.get("roleName").toString() : "";
            if (StringUtils.isNotBlank(name)) {
                list.add(cb.like(root.get("roleName").as(String.class), "%" + name + "%"));
            }
            Predicate[] arr = new Predicate[list.size()];
            arr = list.toArray(arr);
            query.where(arr);
            query.orderBy(cb.asc(gmtCreate));
            TypedQuery<SysRoleDTO> typedQuery = entityManager.createQuery(query);
            int startIndex = size * page;
            typedQuery.setFirstResult(startIndex);
            typedQuery.setMaxResults(params.getSize());
            //总条数
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<SysRoleEntity> root1 = countQuery.from(SysRoleEntity.class);
            countQuery.where(arr);
            countQuery.select(cb.count(root1));
            long count = entityManager.createQuery(countQuery).getSingleResult().longValue();
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            Page<SysRoleDTO> res = new PageImpl<>(typedQuery.getResultList(), pageable, count);
            return res;
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
        return sysRoleRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public SysRoleDTO findRoleById(String id) throws Exception {
        return sysRoleRepo.findRoleById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        return sysUserRoleRepo.findByRoleIdAndUserIdAndIsDeleted(roleId, userId, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public List<SysUserRoleEntity> getSysUserRolesByRoleId(String roleId) throws Exception {
        return sysUserRoleRepo.findByRoleIdAndIsDeleted(roleId, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        return sysRoleMenuRepo.findByRoleIdAndIsDeleted(roleId, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateRoleMenu(SysRoleMenuEntity sysRoleMenuEntity) throws Exception {
        return sysRoleMenuRepo.saveAndFlush(sysRoleMenuEntity).getId();
    }

    @Override
    public List<SysRoleEntity> findAll() {
        return sysRoleRepo.findAll();
    }

    @Override
    public List<SysRoleEntity> findByIsDeleted() {
        return sysRoleRepo.findByIsDeleted(ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public List<String> getLevelOneMenuSids() throws Exception {
        return sysMenuRepo.getLevelOneMenuSids();
    }
}
