package com.jit.server.service.impl;

import com.jit.server.dto.DictDTO;
import com.jit.server.pojo.SysDictionaryEntity;
import com.jit.server.pojo.SysDictionaryItemEntity;
import com.jit.server.repository.DictionaryItemRepo;
import com.jit.server.repository.DictionaryRepo;
import com.jit.server.service.DictionaryService;
import com.jit.server.service.UserService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    DictionaryRepo dictionaryRepo;

    @Autowired
    DictionaryItemRepo dictionaryItemRepo;

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<SysDictionaryEntity> getDictionary(String name, String code, int currentPage, int pageSize) {
        //条件
        Specification<SysDictionaryEntity> spec = new Specification<SysDictionaryEntity>() {
            @Override
            public Predicate toPredicate(Root<SysDictionaryEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));

                if (com.jit.server.util.StringUtils.isNotEmpty(name)) {
                    list.add(cb.like(root.get("dictName").as(String.class), "%" + name + "%"));
                }

                if (StringUtils.isNotEmpty(code)) {
                    list.add(cb.like(root.get("dictCode").as(String.class), "%" + code + "%"));
                }

                Predicate[] arr = new Predicate[list.size()];
                return cb.and(list.toArray(arr));
            }
        };
        //分页的定义
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        return this.dictionaryRepo.findAll(spec , pageable);

    }

    @Override
    public int getCount(String name, String code) {
        int count = dictionaryRepo.getDictionaryCount(name, code);
        return count;
    }

    @Override
    public Optional<SysDictionaryEntity> findById(String id) {
        return dictionaryRepo.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysDictionaryEntity updateDictionary(SysDictionaryEntity sysDictionaryEntity) throws Exception {
        sysDictionaryEntity.setUpdateBy(userService.findIdByUsername());
        return dictionaryRepo.saveAndFlush(sysDictionaryEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysDictionaryEntity addDictionary(SysDictionaryEntity sysDictionaryEntity) throws Exception {
        if (sysDictionaryEntity.getId() != null && sysDictionaryEntity.getId() != "") {
            sysDictionaryEntity.setUpdateBy(userService.findIdByUsername());
            sysDictionaryEntity.setGmtModified(LocalDateTime.now());
        } else {
            sysDictionaryEntity.setCreateBy(userService.findIdByUsername());
            sysDictionaryEntity.setGmtCreate(LocalDateTime.now());
        }
        return dictionaryRepo.saveAndFlush(sysDictionaryEntity);
    }

    @Override
    public SysDictionaryEntity getByDictName(String dictName) {
        return dictionaryRepo.findByDictNameAndIsDeleted(dictName, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public SysDictionaryEntity getByDictCode(String dictCode) {
        return dictionaryRepo.findByDictCodeAndIsDeleted(dictCode, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public SysDictionaryItemEntity getByItemText(String itemName, String dictId) {
        return dictionaryItemRepo.findByItemTextAndDictIdAndIsDeleted(itemName, dictId, ConstUtil.IS_NOT_DELETED);
    }

    @Override
    public List<SysDictionaryItemEntity> getDictionaryByCode(String code) {

        String comditionalSQL = "";
        String baseSQL = "SELECT " +
                "sysdictionaryitementity.id, " +
                "sysdictionaryitementity.dictId, " +
                "sysdictionaryitementity.itemText, " +
                "sysdictionaryitementity.itemValue, " +
                "sysdictionaryitementity.description, " +
                "sysdictionaryitementity.gmtCreate, " +
                "sysdictionaryitementity.gmtModified, " +
                "sysdictionaryitementity.createBy, " +
                "sysdictionaryitementity.updateBy, " +
                "sysdictionaryitementity.sortOrder, " +
                "sysdictionaryitementity.status, " +
                "sysdictionaryitementity.isDeleted " +
                "FROM " +
                "SysDictionaryItemEntity sysdictionaryitementity " +
                "LEFT JOIN SysDictionaryEntity sysdictionaryentity ON sysdictionaryentity.id = sysdictionaryitementity.dictId " +
                "WHERE sysdictionaryentity.isDeleted=0" +
                "AND " +
                "sysdictionaryitementity.isDeleted=0";
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(code)) {
            comditionalSQL += " and sysdictionaryentity.dictCode = :dictCode";
            map.put("dictCode", code);
        }
        //组装SQL
        String resSQL = baseSQL + comditionalSQL;
        Query res = this.entityManager.createQuery(resSQL);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            res.setParameter(entry.getKey(), entry.getValue());
        }
        List<Object[]> resultList = res.getResultList();
        List<SysDictionaryItemEntity> sysDictionaryItemEntities = null;
        if (resultList != null) {
            //创建集合
            sysDictionaryItemEntities = new ArrayList<>();
            for(int i = 0 ; i < resultList.size() ; i++){
                //将对象取出
                Object obj[] = resultList.get(i);
                //创建对象
                SysDictionaryItemEntity sysDictionaryItemEntity = new SysDictionaryItemEntity();
                sysDictionaryItemEntity.setId(obj[0]+"");
                sysDictionaryItemEntity.setDictId(obj[1]+"");
                sysDictionaryItemEntity.setItemText(obj[2]+"");
                sysDictionaryItemEntity.setItemValue(obj[3]+"");
                sysDictionaryItemEntity.setDescription(obj[4]+"");
                sysDictionaryItemEntity.setGmtCreate((java.time.LocalDateTime)obj[5]);
                sysDictionaryItemEntity.setGmtModified((java.time.LocalDateTime)obj[6]);
                sysDictionaryItemEntity.setCreateBy(obj[7]+"");
                sysDictionaryItemEntity.setUpdateBy(obj[8]+"");
                sysDictionaryItemEntity.setSortOrder((int)obj[9]);
                sysDictionaryItemEntity.setStatus((int)obj[10]);
                sysDictionaryItemEntity.setIsDeleted((int)obj[11]);
                sysDictionaryItemEntities.add(sysDictionaryItemEntity);
            }
        }
        return sysDictionaryItemEntities;
    }

    @Override
    public List<DictDTO> getDictByCode(String code) {
        List<Object> dictList = dictionaryItemRepo.getDictByCode(code);
        if (dictList != null && !dictList.isEmpty()) {
            List<DictDTO> dictDTOList = new ArrayList<>(dictList.size());
            Object[] object;
            DictDTO dictDTO;
            for (int i = 0, len = dictList.size(); i < len; i++) {
                object = (Object[]) dictList.get(i);
                dictDTO = new DictDTO();
                dictDTO.setText(object[0] != null ? object[0].toString() : "");
                dictDTO.setValue(object[1] != null ? object[1].toString() : "");
                if (object[2] != null) {
                    if (ConstUtil.STATUS_UNUSED == Integer.parseInt(object[2].toString())) {
                        dictDTO.setStatus(true);
                    } else {
                        dictDTO.setStatus(false);
                    }
                } else {
                    dictDTO.setStatus(true);
                }

                dictDTOList.add(dictDTO);
            }
            return dictDTOList;
        } else {
            return null;
        }
    }

    @Override
    public Page<SysDictionaryItemEntity> findByDictId(String id, String itemText, int status, int currentPage, int pageSize) {
        //条件
        Specification<SysDictionaryItemEntity> spec = new Specification<SysDictionaryItemEntity>() {
            @Override
            public Predicate toPredicate(Root<SysDictionaryItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));

                list.add(cb.equal(root.get("dictId").as(String.class),  id ));

                if (StringUtils.isNotEmpty(itemText)) {
                    list.add(cb.like(root.get("itemText").as(String.class), "%" + itemText + "%"));
                }
                if (status != -1) {
                    list.add(cb.equal(root.get("status").as(Integer.class), status));
                }
                Predicate[] arr = new Predicate[list.size()];
                return cb.and(list.toArray(arr));
            }
        };
        //排序的定义
        List<Sort.Order> list = new ArrayList<>();
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "sortOrder");
        list.add(order1);
        Sort sort = Sort.by(list);
        //分页的定义
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        return this.dictionaryItemRepo.findAll(spec , pageable);

    }

    @Override
    public int getDictionaryItemCount(String id, String itemText, int status) {
        int count = dictionaryItemRepo.getDictionaryCount(id, itemText, status);
        return count;
    }


    @Override
    public Optional<SysDictionaryItemEntity> findDictionaryItemById(String id) {
        return dictionaryItemRepo.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysDictionaryItemEntity updateDictionaryItem(SysDictionaryItemEntity sysDictionaryItemEntity) throws Exception {
        sysDictionaryItemEntity.setUpdateBy(userService.findIdByUsername());
        return dictionaryItemRepo.save(sysDictionaryItemEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysDictionaryItemEntity addDictionaryItem(SysDictionaryItemEntity sysDictionaryItemEntity) throws Exception {
        if (sysDictionaryItemEntity.getId() != null && sysDictionaryItemEntity.getId() != "") {
            sysDictionaryItemEntity.setUpdateBy(userService.findIdByUsername());
            sysDictionaryItemEntity.setGmtModified(LocalDateTime.now());
        } else {
            sysDictionaryItemEntity.setCreateBy(userService.findIdByUsername());
            sysDictionaryItemEntity.setGmtCreate(LocalDateTime.now());
        }
        return dictionaryItemRepo.save(sysDictionaryItemEntity);
    }

    @Override
    public String getItemTextByDictCodeAndItemValue(String dictCode, String itemValue) throws Exception {
        return dictionaryItemRepo.findItemTextByDictCodeAndItemValue(dictCode, itemValue);
    }
}
