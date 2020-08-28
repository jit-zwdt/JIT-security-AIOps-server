package com.jit.server.service.impl;

import com.jit.server.pojo.SysRoleEntity;
import com.jit.server.repository.SysRoleRepo;
import com.jit.server.service.SysRoleService;
import com.jit.server.service.UserService;
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
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepo sysRoleRepo;

    @Autowired
    private UserService userService;

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
}
