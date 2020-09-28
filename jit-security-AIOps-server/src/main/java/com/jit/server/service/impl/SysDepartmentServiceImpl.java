package com.jit.server.service.impl;

import com.jit.server.dto.TreeNode;
import com.jit.server.pojo.SysDepartmentEntity;
import com.jit.server.repository.SysDepartmentRepo;
import com.jit.server.service.SysDepartmentService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysDepartmentServiceImpl implements SysDepartmentService {

    @Autowired
    private SysDepartmentRepo sysDepartmentRepo;

    @Override
    public List<SysDepartmentEntity> findByParentIdAndIsDeletedOrderByDepartOrderAsc(String parentId, int isDeleted) throws Exception {
        return sysDepartmentRepo.findByParentIdAndIsDeletedOrderByDepartOrderAsc(parentId, isDeleted);
    }

    @Override
    public List<Object> getTreeNode(String parentId) throws Exception {
        return sysDepartmentRepo.getTreeNode(parentId);
    }

    @Override
    public List<TreeNode> getDepartmentInfos() throws Exception {
        return getTreeNodeList("0");
    }

    private List<TreeNode> getTreeNodeList(String parentId) {
        List<TreeNode> nodes = new ArrayList<>();
        List<Object> nodelist = sysDepartmentRepo.getTreeNode(parentId);
        if (nodelist != null && !nodelist.isEmpty()) {
            TreeNode treeNode;
            for (Object object : nodelist) {
                treeNode = new TreeNode();
                Object[] obj = (Object[]) object;
                treeNode.setId(StringUtils.getVal(obj[0]));
                treeNode.setLabel(StringUtils.getVal(obj[1]));
                treeNode.setParent("0".equals(parentId) ? true : false);
                treeNode.setParentId(parentId);
                treeNode.setChildren(getTreeNodeList(StringUtils.getVal(obj[0])));
                nodes.add(treeNode);
            }
        }
        return nodes;
    }

    @Override
    public String saveOrUpdateDepartment(SysDepartmentEntity department) throws Exception {
        if (department != null) {
            return sysDepartmentRepo.saveAndFlush(department).getId();
        } else {
            return null;
        }
    }

    @Override
    public SysDepartmentEntity getDepartment(String id) throws Exception {
        return sysDepartmentRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public List<String> getSubDepIds(String parentId) throws Exception {
        return sysDepartmentRepo.getSubDepIds(parentId);
    }

    @Override
    public int delDepartmentsByIds(List<String> list, LocalDateTime gmtModified, String updateBy) throws Exception {
        return sysDepartmentRepo.delDepartmentsByIds(list, gmtModified, updateBy);
    }

    @Override
    public SysDepartmentEntity getDepartmentByDepartCode(String code) {
        return sysDepartmentRepo.findByDepartCodeAndIsDeleted(code,ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public List<SysDepartmentEntity> getAllDepartment() {
        return sysDepartmentRepo.findByIsDeleted(ConstUtil.IS_NOT_DELETED);
    }
}
