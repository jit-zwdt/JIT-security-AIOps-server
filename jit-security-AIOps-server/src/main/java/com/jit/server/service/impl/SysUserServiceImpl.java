package com.jit.server.service.impl;

import com.jit.server.pojo.SysUserEntity;
import com.jit.server.repository.SysUserRepo;
import com.jit.server.request.SysUserEntityParams;
import com.jit.server.service.SysUserService;
import com.jit.server.service.UserService;
import com.jit.server.util.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepo sysUserRepo;

    @Autowired
    private UserService userService;

    @Override
    public String findIdByUsername() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (StringUtils.isNotEmpty(username)) {
            return sysUserRepo.findIdByUsername(username);
        } else {
            return null;
        }
    }

    @Override
    public Page<SysUserEntity> getUsers(PageRequest<SysUserEntityParams> params) {
        SysUserEntityParams param = params.getParam();
        if (param != null) {
            //条件
            Specification<SysUserEntity> spec = new Specification<SysUserEntity>() {
                @Override
                public Predicate toPredicate(Root<SysUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> list = new ArrayList<Predicate>();
                    list.add(cb.equal(root.get("isDeleted").as(String.class), "0"));
                    if (StringUtils.isNotBlank(param.getUsername())) {
                        list.add(cb.equal(root.get("username").as(String.class), param.getUsername()));
                    }
                    if (StringUtils.isNotBlank(param.getName())) {
                        list.add(cb.equal(root.get("name").as(String.class), param.getName()));
                    }
                    if (StringUtils.isNotBlank(param.getStatus())) {
                        list.add(cb.equal(root.get("status").as(String.class), param.getStatus()));
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
            return sysUserRepo.findAll(spec, pageable);
        }
        return null;
    }

    @Override
    public List<SysUserEntity> findUserByRole(String roleId) throws Exception {
        if (StringUtils.isNotEmpty(roleId)) {
            return sysUserRepo.findUserByRole(roleId);
        } else {
            return null;
        }
    }

    @Override
    public SysUserEntity addUser(SysUserEntity params) throws Exception {
        if(params.getId() != null && params.getId() != ""){
            params.setUpdateBy(userService.findIdByUsername());
            params.setGmtModified(new java.sql.Timestamp(System.currentTimeMillis()));
        }else{
            if(params.getPassword() != null && params.getPassword() != ""){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                String password = bCryptPasswordEncoder.encode(params.getPassword());
                params.setPassword(password);
            }
            params.setCreateBy(userService.findIdByUsername());
            params.setGmtCreate(new java.sql.Timestamp(System.currentTimeMillis()));
        }
        return  sysUserRepo.save(params);
    }

    @Override
    public SysUserEntity updatePassword(SysUserEntity params) throws Exception {
        if(params.getId() != null && params.getId() != ""){
            if(params.getPassword() != null && params.getPassword() != ""){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                String password = bCryptPasswordEncoder.encode(params.getPassword());
                params.setPassword(password);
            }
            params.setUpdateBy(userService.findIdByUsername());
            params.setGmtModified(new java.sql.Timestamp(System.currentTimeMillis()));
        }
        return  sysUserRepo.save(params);
    }

    @Override
    public Optional<SysUserEntity> findById(String id) {
        return sysUserRepo.findById(id);
    }

    @Override
    public SysUserEntity getByUserName(String username) {
        return sysUserRepo.findByUsernameAndIsDeleted(username,0);
    }
}
