package com.jit.server.repository;

import com.jit.server.pojo.SysDictionaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DictionaryRepo extends JpaRepository<SysDictionaryEntity, String>, JpaSpecificationExecutor<SysDictionaryEntity> {

    @Query(value = "select * from sys_dict e where e.is_deleted = 0 and if(?1 != '',e.dict_name like %?1%,1=1)  and if(?2 != '',e.dict_code like %?2%,1=1) limit ?3,?4",nativeQuery = true)
    List<SysDictionaryEntity> getDictionary(String name,String code,int start,int pageSize);

    @Query(value = "select count(*) from sys_dict e where e.is_deleted = 0 and if(?1 != '',e.dict_name like %?1%,1=1)  and if(?2 != '',e.dict_code like %?2%,1=1)",nativeQuery = true)
    int getDictionaryCount(String name,String code);
}
