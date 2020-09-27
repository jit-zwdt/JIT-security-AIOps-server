package com.jit.server.service;

import com.jit.server.dto.TreeNode;
import com.jit.server.pojo.SysDepartmentEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface SysDepartmentService {

    List<SysDepartmentEntity> findByParentIdAndIsDeletedOrderByDepartOrderAsc(String parentId, int isDeleted) throws Exception;

    List<Object> getTreeNode(String parentId) throws Exception;

    List<TreeNode> getDepartmentInfos() throws Exception;

    String saveOrUpdateDepartment(SysDepartmentEntity department) throws Exception;

    SysDepartmentEntity getDepartment(String id) throws Exception;

    List<String> getSubDepIds(String id) throws Exception;

    int delDepartmentsByIds(List<String> list, LocalDateTime gmtModified, String updateBy) throws Exception;

    SysDepartmentEntity getDepartmentByDepartCode(String code);

    List<SysDepartmentEntity> getAllDepartment();
}
