package com.jit.server.service;

import com.jit.server.dto.DictDTO;
import com.jit.server.pojo.SysDictionaryEntity;
import com.jit.server.pojo.SysDictionaryItemEntity;

import java.util.List;
import java.util.Optional;

public interface DictionaryService {
    List<SysDictionaryEntity> getDictionary(String name, String code, int currentPage, int pageSize);

    int getCount(String name, String code);

    Optional<SysDictionaryEntity> findById(String id);

    SysDictionaryEntity updateDictionary(SysDictionaryEntity sysDictionaryEntity) throws Exception;

    SysDictionaryEntity addDictionary(SysDictionaryEntity sysDictionaryEntity) throws Exception;

    List<SysDictionaryItemEntity> findByDictId(String id, String itemName, int status, int currentPage, int pageSize);

    Optional<SysDictionaryItemEntity> findDictionaryItemById(String id);

    SysDictionaryItemEntity updateDictionaryItem(SysDictionaryItemEntity sysDictionaryItemEntity) throws Exception;

    SysDictionaryItemEntity addDictionaryItem(SysDictionaryItemEntity sysDictionaryItemEntity) throws Exception;

    int getDictionaryItemCount(String id, String itemText, int status);

    SysDictionaryEntity getByDictName(String dictName);

    SysDictionaryEntity getByDictCode(String dictCode);

    SysDictionaryItemEntity getByItemText(String itemName, String dictId);

    List<SysDictionaryItemEntity> getDictionaryByCode(String code);

    List<DictDTO> getDictByCode(String code) throws Exception;

    /**
     * 根据字典编码和子项值获取子项名称
     *
     * @param dictCode  这个应该从也页面获取
     * @param itemValue
     * @return
     * @throws Exception
     */
    String getItemTextByDictCodeAndItemValue(String dictCode, String itemValue) throws Exception;
}
