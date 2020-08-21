package com.jit.server.service.impl;

import com.jit.server.pojo.SysDictionaryEntity;
import com.jit.server.pojo.SysDictionaryItemEntity;
import com.jit.server.repository.DictionaryItemRepo;
import com.jit.server.repository.DictionaryRepo;
import com.jit.server.service.DictionaryService;
import com.jit.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public List<SysDictionaryEntity> getDictionary(String name,String code,int currentPage,int pageSize) {
        int start = (currentPage-1)*pageSize;
        List<SysDictionaryEntity> list = dictionaryRepo.getDictionary(name,code,start,pageSize);
        return list;
    }

    @Override
    public int getCount(String name, String code) {
        int count = dictionaryRepo.getDictionaryCount(name,code);
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
        if(sysDictionaryEntity.getId() != null && sysDictionaryEntity.getId() != ""){
            sysDictionaryEntity.setUpdateBy(userService.findIdByUsername());
            sysDictionaryEntity.setGmtModified(new java.sql.Timestamp(new Date().getTime()));
        }else{
            sysDictionaryEntity.setCreateBy(userService.findIdByUsername());
            sysDictionaryEntity.setGmtCreate(new java.sql.Timestamp(new Date().getTime()));
        }
        return  dictionaryRepo.save(sysDictionaryEntity);
    }

    @Override
    public SysDictionaryEntity getByDictName(String dictName) {
        return dictionaryRepo.findByDictNameAndIsDeleted(dictName,0);
    }

    @Override
    public SysDictionaryEntity getByDictCode(String dictCode) {
        return dictionaryRepo.findByDictCodeAndIsDeleted(dictCode,0);
    }

    @Override
    public SysDictionaryItemEntity getByItemText(String itemName,String dictId) {
        return dictionaryItemRepo.findByItemTextAndDictIdAndIsDeleted(itemName,dictId,0);
    }

    @Override
    public List<SysDictionaryItemEntity> findByDictId(String id,String itemText,int status,int currentPage,int pageSize) {
        int start = (currentPage-1)*pageSize;
        return dictionaryItemRepo.findByDictId(id,itemText,status,start,pageSize);
    }

    @Override
    public int getDictionaryItemCount(String id,String itemText,int status) {
        int count = dictionaryItemRepo.getDictionaryCount(id,itemText,status);
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
        if(sysDictionaryItemEntity.getId() != null && sysDictionaryItemEntity.getId() != ""){
            sysDictionaryItemEntity.setUpdateBy(userService.findIdByUsername());
            sysDictionaryItemEntity.setGmtModified(new java.sql.Timestamp(new Date().getTime()));
        }else{
            sysDictionaryItemEntity.setCreateBy(userService.findIdByUsername());
            sysDictionaryItemEntity.setGmtCreate(new java.sql.Timestamp(new Date().getTime()));
        }
        return  dictionaryItemRepo.save(sysDictionaryItemEntity);
    }
}
