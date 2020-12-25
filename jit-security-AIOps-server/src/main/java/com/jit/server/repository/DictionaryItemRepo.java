package com.jit.server.repository;

import com.jit.server.pojo.SysDictionaryEntity;
import com.jit.server.pojo.SysDictionaryItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DictionaryItemRepo extends JpaRepository<SysDictionaryItemEntity, String>, JpaSpecificationExecutor<SysDictionaryItemEntity> {

//    @Query(value = "select * from sys_dict_item e where e.is_deleted = 0 and e.dict_id = ?1 and if(?2 != '',e.item_text like %?2%,1=1)  and if(?3 != -1,e.status = ?3,1=1) order by e.sort_order limit ?4,?5", nativeQuery = true)
//    List<SysDictionaryItemEntity> findByDictId(String id, String itemText, int status, int currentPage, int pageSize);

    @Override
    Page<SysDictionaryItemEntity> findAll(Specification<SysDictionaryItemEntity> spec, Pageable pageable);

    @Query(value = "select count(*) from sys_dict_item e where  e.is_deleted = 0 and e.dict_id = ?1 and if(?2 != '',e.item_text like %?2%,1=1)  and if(?3 != -1,e.status = ?3,1=1)", nativeQuery = true)
    int getDictionaryCount(String id, String itemText, int status);

    SysDictionaryItemEntity findByItemTextAndDictIdAndIsDeleted(String itemText, String dictId, int isDeleted);

//    @Query(value = "select di.* from sys_dict_item di,sys_dict d where di.is_deleted = 0 and d.is_deleted = 0 and di.dict_id = d.id and if(?1 != '',d.dict_code = ?1,1=1)", nativeQuery = true)
//    List<SysDictionaryItemEntity> getDictionaryByCode(String code);

    @Query(value = "select di.item_text,di.item_value,di.status from sys_dict_item di,sys_dict d where di.is_deleted = 0 and d.is_deleted = 0 and di.dict_id = d.id and d.dict_code = ?1 order by di.sort_order", nativeQuery = true)
    List<Object> getDictByCode(String code);

    @Query(value = "select di.item_text from sys_dict_item di,sys_dict d where di.is_deleted = 0 and d.is_deleted = 0 and di.dict_id = d.id and d.dict_code = ?1 and di.item_value = ?2 ", nativeQuery = true)
    String findItemTextByDictCodeAndItemValue(String dictCode, String itemValue);
}
