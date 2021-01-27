package com.jit.server.service;

import com.jit.server.dto.DictDTO;
import com.jit.server.dto.SysDictionaryDTO;
import com.jit.server.dto.SysDictionaryItemDTO;
import com.jit.server.pojo.SysDictionaryEntity;
import com.jit.server.pojo.SysDictionaryItemEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DictionaryService {
    Page<SysDictionaryDTO> getDictionary(String name, String code, int currentPage, int pageSize);

    int getCount(String name, String code) throws Exception;

    SysDictionaryDTO findSysDictionaryById(String id) throws Exception;

    SysDictionaryEntity updateDictionary(SysDictionaryEntity sysDictionaryEntity) throws Exception;

    SysDictionaryEntity addDictionary(SysDictionaryEntity sysDictionaryEntity) throws Exception;

    Page<SysDictionaryItemDTO> findByDictId(String id, String itemName, int status, int currentPage, int pageSize) throws Exception;

    Optional<SysDictionaryItemEntity> findDictionaryItemById(String id) throws Exception;

    SysDictionaryItemEntity updateDictionaryItem(SysDictionaryItemEntity sysDictionaryItemEntity) throws Exception;

    SysDictionaryItemEntity addDictionaryItem(SysDictionaryItemEntity sysDictionaryItemEntity) throws Exception;

    int getDictionaryItemCount(String id, String itemText, int status) throws Exception;

    SysDictionaryEntity getByDictName(String dictName) throws Exception;

    SysDictionaryEntity getByDictCode(String dictCode) throws Exception;

    SysDictionaryItemEntity getByItemText(String itemName, String dictId) throws Exception;

    List<SysDictionaryItemDTO> getDictionaryItemsByCode(String code) throws Exception;

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

    Optional<SysDictionaryEntity> findById(String id) throws Exception;

    SysDictionaryItemDTO findDictItemById(String id) throws Exception;
}
