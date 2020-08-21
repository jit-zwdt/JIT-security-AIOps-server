package com.jit.server.repository;

import com.jit.server.pojo.SysDictionaryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DictionaryItemRepo  extends JpaRepository<SysDictionaryItemEntity, String>, JpaSpecificationExecutor<SysDictionaryItemEntity> {

    @Query(value = "select * from sys_dict_item e where e.is_deleted = 0 and e.dict_id = ?1 and if(?2 != '',e.item_text like %?2%,1=1)  and if(?3 != -1,e.status = ?3,1=1) order by e.sort_order limit ?4,?5",nativeQuery = true)
    List<SysDictionaryItemEntity> findByDictId(String id,String itemText,int status,int currentPage,int pageSize);

    @Query(value = "select count(*) from sys_dict_item e where  e.is_deleted = 0 and e.dict_id = ?1 and if(?2 != '',e.item_text like %?2%,1=1)  and if(?3 != -1,e.status = ?3,1=1)",nativeQuery = true)
    int getDictionaryCount(String id,String itemText,int status);

    SysDictionaryItemEntity findByItemTextAndDictIdAndIsDeleted(String itemText,String dictId,int isDeleted);
}
