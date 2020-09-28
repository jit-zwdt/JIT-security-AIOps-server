package com.jit.server.service.impl;

import com.jit.server.pojo.SysDictionaryEntity;
import com.jit.server.pojo.SysDictionaryItemEntity;
import com.jit.server.repository.DictionaryItemRepo;
import com.jit.server.repository.DictionaryRepo;
import com.jit.server.service.DictionaryService;
import com.jit.server.service.UserService;
import com.jit.server.util.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    DictionaryRepo dictionaryRepo;

    @Autowired
    DictionaryItemRepo dictionaryItemRepo;

    @Autowired
    private UserService userService;

    @Override
    public List<SysDictionaryEntity> getDictionary(String name, String code, int currentPage, int pageSize) {
        int start = (currentPage - 1) * pageSize;
        List<SysDictionaryEntity> list = dictionaryRepo.getDictionary(name, code, start, pageSize);
        return list;
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
    public SysDictionaryEntity updateDictionary(SysDictionaryEntity sysDictionaryEntity) throws Exception {
        sysDictionaryEntity.setUpdateBy(userService.findIdByUsername());
        return dictionaryRepo.save(sysDictionaryEntity);
    }

    @Override
    public SysDictionaryEntity addDictionary(SysDictionaryEntity sysDictionaryEntity) throws Exception {
        if (sysDictionaryEntity.getId() != null && sysDictionaryEntity.getId() != "") {
            sysDictionaryEntity.setUpdateBy(userService.findIdByUsername());
            sysDictionaryEntity.setGmtModified(LocalDateTime.now());
        } else {
            sysDictionaryEntity.setCreateBy(userService.findIdByUsername());
            sysDictionaryEntity.setGmtCreate(LocalDateTime.now());
        }
        return dictionaryRepo.save(sysDictionaryEntity);
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
        return dictionaryItemRepo.getDictionaryByCode(code);
    }

    @Override
    public List<SysDictionaryItemEntity> findByDictId(String id, String itemText, int status, int currentPage, int pageSize) {
        int start = (currentPage - 1) * pageSize;
        return dictionaryItemRepo.findByDictId(id, itemText, status, start, pageSize);
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
    public SysDictionaryItemEntity updateDictionaryItem(SysDictionaryItemEntity sysDictionaryItemEntity) throws Exception {
        sysDictionaryItemEntity.setUpdateBy(userService.findIdByUsername());
        return dictionaryItemRepo.save(sysDictionaryItemEntity);
    }

    @Override
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
}
