package com.jit.server.repository;

import com.jit.server.dto.SysDictionaryDTO;
import com.jit.server.pojo.SysDictionaryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface DictionaryRepo extends JpaRepository<SysDictionaryEntity, String>, JpaSpecificationExecutor<SysDictionaryEntity> {

    @Override
    Page<SysDictionaryEntity> findAll(Specification<SysDictionaryEntity> spec, Pageable pageable);

    @Query(value = "select count(*) from sys_dict e where e.is_deleted = 0 and if(?1 != '',e.dict_name like %?1%,1=1)  and if(?2 != '',e.dict_code like %?2%,1=1)", nativeQuery = true)
    int getDictionaryCount(String name, String code);

    SysDictionaryEntity findByDictNameAndIsDeleted(String dictName, int isDeleted);

    SysDictionaryEntity findByDictCodeAndIsDeleted(String dictCode, int isDeleted);

    @Query("select new com.jit.server.dto.SysDictionaryDTO(d.id, d.dictName, d.dictCode, d.description) from SysDictionaryEntity d where d.isDeleted = 0 and d.id = ?1")
    SysDictionaryDTO findSysDictionaryById(String id);
}
