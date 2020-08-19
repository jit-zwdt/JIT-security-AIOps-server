package com.jit.server.service;

import com.jit.server.dto.TreeNode;
import com.jit.server.pojo.SysDepartmentEntity;

import java.util.List;

public interface DepartmentService {

    List<SysDepartmentEntity> findByParentIdAndIsDeletedOrderByDepartOrderAsc(String parentId, int isDeleted) throws Exception;

    List<Object> getTreeNode(String parentId) throws Exception;

    List<TreeNode> getDepartmentInfos() throws Exception;

    String saveOrUpdateDepartment(SysDepartmentEntity department) throws Exception;
}
