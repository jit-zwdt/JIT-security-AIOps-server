package com.jit.server.service.impl;

import com.jit.server.dto.DictDTO;
import com.jit.server.dto.SysDictionaryDTO;
import com.jit.server.dto.SysDictionaryItemDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    public Page<SysDictionaryDTO> getDictionary(String name, String code, int page, int size) {
        page = page - 1;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysDictionaryDTO> query = cb.createQuery(SysDictionaryDTO.class);
        Root<SysDictionaryEntity> root = query.from(SysDictionaryEntity.class);
        Path<String> id = root.get("id");
        Path<String> dictName = root.get("dictName");
        Path<String> dictCode = root.get("dictCode");
        Path<String> description = root.get("description");
        Path<LocalDateTime> gmtCreate = root.get("gmtCreate");
        //查询字段
        query.multiselect(id, dictName, dictCode, description);
        //查询条件
        List<Predicate> list = new ArrayList<>();
        list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
        if (com.jit.server.util.StringUtils.isNotEmpty(name)) {
            list.add(cb.like(dictName.as(String.class), "%" + name + "%"));
        }

        if (StringUtils.isNotEmpty(code)) {
            list.add(cb.like(dictCode.as(String.class), "%" + code + "%"));
        }
        Predicate[] arr = new Predicate[list.size()];
        arr = list.toArray(arr);
        query.where(arr);
        query.orderBy(cb.asc(gmtCreate));
        TypedQuery<SysDictionaryDTO> typedQuery = entityManager.createQuery(query);
        int startIndex = size * page;
        typedQuery.setFirstResult(startIndex);
        typedQuery.setMaxResults(size);
        //总条数
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<SysDictionaryEntity> root1 = countQuery.from(SysDictionaryEntity.class);
        countQuery.where(arr);
        countQuery.select(cb.count(root1));
        long count = entityManager.createQuery(countQuery).getSingleResult().longValue();
        //分页的定义
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Page<SysDictionaryDTO> res = new PageImpl<>(typedQuery.getResultList(), pageable, count);
        return res;
    }

    @Override
    public int getCount(String name, String code) {
        int count = dictionaryRepo.getDictionaryCount(name, code);
        return count;
    }

    @Override
    public SysDictionaryDTO findSysDictionaryById(String id) {
        return dictionaryRepo.findSysDictionaryById(id);
    }

    @Override
    public Optional<SysDictionaryEntity> findById(String id) throws Exception {
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
    public List<SysDictionaryItemDTO> getDictionaryItemsByCode(String code) {

        String comditionalSQL = "";
        String baseSQL = "SELECT " +
                "sysdictionaryitementity.id, " +
                "sysdictionaryitementity.dictId, " +
                "sysdictionaryitementity.itemText, " +
                "sysdictionaryitementity.itemValue, " +
                "sysdictionaryitementity.description, " +
                "sysdictionaryitementity.sortOrder, " +
                "sysdictionaryitementity.status " +
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
        List<SysDictionaryItemDTO> sysDictionaryItemDTOS = null;
        if (resultList != null) {
            //创建集合
            sysDictionaryItemDTOS = new ArrayList<>();
            for (int i = 0; i < resultList.size(); i++) {
                //将对象取出
                Object obj[] = resultList.get(i);
                //创建对象
                SysDictionaryItemDTO sysDictionaryItemDTO = new SysDictionaryItemDTO();
                sysDictionaryItemDTO.setId(obj[0] + "");
                sysDictionaryItemDTO.setDictId(obj[1] + "");
                sysDictionaryItemDTO.setItemText(obj[2] + "");
                sysDictionaryItemDTO.setItemValue(obj[3] + "");
                sysDictionaryItemDTO.setDescription(obj[4] + "");
                sysDictionaryItemDTO.setSortOrder((int) obj[5]);
                sysDictionaryItemDTO.setStatus((int) obj[6]);
                sysDictionaryItemDTOS.add(sysDictionaryItemDTO);
            }
        }
        return sysDictionaryItemDTOS;
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
    public Page<SysDictionaryItemDTO> findByDictId(String pid, String text, int intStatus, int page, int size) {
        page = page - 1;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysDictionaryItemDTO> query = cb.createQuery(SysDictionaryItemDTO.class);
        Root<SysDictionaryItemEntity> root = query.from(SysDictionaryItemEntity.class);
        Path<String> id = root.get("id");
        Path<String> dictId = root.get("dictId");
        Path<String> itemText = root.get("itemText");
        Path<String> itemValue = root.get("itemValue");
        Path<String> description = root.get("description");
        Path<Integer> sortOrder = root.get("sortOrder");
        Path<Integer> status = root.get("status");
        //查询字段
        query.multiselect(id, dictId, itemText, itemValue, description, sortOrder, status);
        //查询条件
        List<Predicate> list = new ArrayList<>();
        list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
        list.add(cb.equal(dictId.as(String.class), pid));

        if (StringUtils.isNotEmpty(text)) {
            list.add(cb.like(itemText.as(String.class), "%" + text + "%"));
        }
        if (intStatus != -1) {
            list.add(cb.equal(status.as(Integer.class), intStatus));
        }
        Predicate[] arr = new Predicate[list.size()];
        arr = list.toArray(arr);
        query.where(arr);
        query.orderBy(cb.asc(sortOrder));
        TypedQuery<SysDictionaryItemDTO> typedQuery = entityManager.createQuery(query);
        int startIndex = size * page;
        typedQuery.setFirstResult(startIndex);
        typedQuery.setMaxResults(size);
        //总条数
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<SysDictionaryItemEntity> root1 = countQuery.from(SysDictionaryItemEntity.class);
        countQuery.where(arr);
        countQuery.select(cb.count(root1));
        long count = entityManager.createQuery(countQuery).getSingleResult().longValue();
        //分页的定义
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Page<SysDictionaryItemDTO> res = new PageImpl<>(typedQuery.getResultList(), pageable, count);
        return res;
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
    public SysDictionaryItemDTO findDictItemById(String id) throws Exception {
        return dictionaryItemRepo.findDictItemById(id);
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
