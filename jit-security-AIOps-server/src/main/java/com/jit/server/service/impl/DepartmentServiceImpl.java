package com.jit.server.service.impl;

import com.jit.server.dto.TreeNode;
import com.jit.server.pojo.SysDepartmentEntity;
import com.jit.server.repository.SysDepartmentRepo;
import com.jit.server.service.DepartmentService;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

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
        return sysDepartmentRepo.findByIdAndIsDeleted(id,0);
    }
}
