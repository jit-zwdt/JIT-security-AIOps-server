package com.jit.server.service.impl;

import com.jit.server.dto.SysUserDTO;
import com.jit.server.pojo.SysUserEntity;
import com.jit.server.repository.SysUserRepo;
import com.jit.server.request.SysUserEntityParams;
import com.jit.server.service.SysUserService;
import com.jit.server.service.UserService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepo sysUserRepo;

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

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
    public Page<SysUserDTO> getUsers(PageRequest<SysUserEntityParams> params) {
        SysUserEntityParams param = params.getParam();
        if (param != null) {
            int size = params.getSize();
            int page = params.getPage() - 1;
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SysUserDTO> query = cb.createQuery(SysUserDTO.class);
            Root<SysUserEntity> root = query.from(SysUserEntity.class);
            Path<String> id = root.get("id");
            Path<String> username = root.get("username");
            Path<String> name = root.get("name");
            Path<String> workNo = root.get("workNo");
            Path<String> mobile = root.get("mobile");
            Path<Integer> sex = root.get("sex");
            Path<LocalDate> birth = root.get("birth");
            Path<String> picId = root.get("picId");
            Path<String> picUrl = root.get("picUrl");
            Path<String> departmentId = root.get("departmentId");
            Path<String> province = root.get("province");
            Path<String> city = root.get("city");
            Path<String> liveAddress = root.get("liveAddress");
            Path<String> hobby = root.get("hobby");
            Path<String> email = root.get("email");
            Path<Integer> status = root.get("status");
            Path<LocalDateTime> gmtCreate = root.get("gmtCreate");
            //查询字段
            query.multiselect(id, username, name, workNo, mobile, sex, birth, picId, picUrl, departmentId, province, city, liveAddress, hobby, email, status);
            //查询条件
            List<Predicate> list = new ArrayList<Predicate>();
            list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
            if (StringUtils.isNotBlank(param.getUsername())) {
                list.add(cb.like(root.get("username").as(String.class), '%' + param.getUsername() + '%'));
            }
            if (StringUtils.isNotBlank(param.getName())) {
                list.add(cb.like(root.get("name").as(String.class), '%' + param.getName() + '%'));
            }
            if (StringUtils.isNotBlank(param.getStatus())) {
                list.add(cb.equal(root.get("status").as(String.class), param.getStatus()));
            }
            Predicate[] arr = new Predicate[list.size()];
            arr = list.toArray(arr);
            query.where(arr);
            query.orderBy(cb.asc(gmtCreate));
            TypedQuery<SysUserDTO> typedQuery = entityManager.createQuery(query);
            //设置分页参数
            int startIndex = size * page;
            typedQuery.setFirstResult(startIndex);
            typedQuery.setMaxResults(params.getSize());
            //总条数
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<SysUserEntity> root1 = countQuery.from(SysUserEntity.class);
            countQuery.where(arr);
            countQuery.select(cb.count(root1));
            long count = entityManager.createQuery(countQuery).getSingleResult().longValue();
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            Page<SysUserDTO> res = new PageImpl<>(typedQuery.getResultList(), pageable, count);
            return res;
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
    @Transactional(rollbackFor = Exception.class)
    public void addUser(SysUserEntity params) throws Exception {
        if ("".equals(params.getId()) || params.getId() == null) {
            if (params.getPassword() != null && !"".equals(params.getPassword())) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                String password = bCryptPasswordEncoder.encode(params.getPassword());
                params.setPassword(password);
            }
            params.setCreateBy(userService.findIdByUsername());
            params.setGmtCreate(LocalDateTime.now());
            sysUserRepo.save(params);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserDTO params) throws Exception {
        if (params.getId() != null && params.getId() != "") {
            SysUserEntity sysUserEntity = sysUserRepo.findByIdAndIsDeleted(params.getId(), ConstUtil.IS_NOT_DELETED);
            if (sysUserEntity != null) {
                BeanUtils.copyProperties(params, sysUserEntity);
                sysUserEntity.setUpdateBy(userService.findIdByUsername());
                sysUserEntity.setGmtModified(LocalDateTime.now());
                sysUserRepo.saveAndFlush(sysUserEntity);
            } else {
                throw new Exception("data is not existed");
            }

        } else {
            throw new Exception("id is null");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(SysUserEntity params) throws Exception {
        if (params.getId() != null && params.getId() != "") {
            SysUserEntity sysUserEntity = sysUserRepo.findByIdAndIsDeleted(params.getId(), ConstUtil.IS_NOT_DELETED);
            if (sysUserEntity != null) {
                if (params.getPassword() != null && params.getPassword() != "") {
                    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                    String password = bCryptPasswordEncoder.encode(params.getPassword());
                    sysUserEntity.setPassword(password);
                    sysUserEntity.setUpdateBy(userService.findIdByUsername());
                    sysUserEntity.setGmtModified(LocalDateTime.now());
                    sysUserRepo.save(sysUserEntity);
                }
            } else {
                throw new Exception("data is not existed");
            }
        } else {
            throw new Exception("id is null");
        }
    }

    @Override
    public SysUserEntity findById(String id) {
        return sysUserRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public SysUserEntity getByUserName(String username) {
        return sysUserRepo.findByUsernameAndIsDeleted(username, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public SysUserDTO findUserById(String id) {
        return sysUserRepo.findUserById(id);
    }

    @Override
    public void deleteUser(String id) throws Exception {
        SysUserEntity sysUserEntity = sysUserRepo.findByIdAndIsDeleted(id, ConstUtil.IS_NOT_DELETED);
        if (sysUserEntity != null) {
            sysUserEntity.setIsDeleted(ConstUtil.IS_DELETED);
            sysUserEntity.setUpdateBy(userService.findIdByUsername());
            sysUserEntity.setGmtModified(LocalDateTime.now());
            sysUserRepo.saveAndFlush(sysUserEntity);
        } else {
            throw new Exception("data is not existed");
        }
    }
}
