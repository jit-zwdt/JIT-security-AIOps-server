package com.jit.server.controller;


import com.jit.server.dto.DictionaryDTO;
import com.jit.server.dto.DictionaryItemResultDTO;
import com.jit.server.dto.DictionaryResultDTO;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysDictionaryEntity;
import com.jit.server.pojo.SysDictionaryItemEntity;
import com.jit.server.service.DictionaryService;
import com.jit.server.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sys/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @ResponseBody
    @PostMapping(value = "/getDictionary")
    public Result getDictionary(@RequestParam("name")String name, @RequestParam("code")String code, @RequestParam("currentPage")String currentPage, @RequestParam("pageSize")String pageSize) {
        int currentPageTemp = 0;
        if(currentPage != "" && currentPage != null){
            currentPageTemp = Integer.parseInt(currentPage);
        }
        int pageSizeTemp = 0;
        if(pageSize != "" && pageSize != null){
            pageSizeTemp = Integer.parseInt(pageSize);
        }
        try {
            List<DictionaryDTO> list = new ArrayList<>();
            DictionaryResultDTO dictionaryResultDTO = new DictionaryResultDTO();
            List<SysDictionaryEntity> sysDictionaryEntityList = dictionaryService.getDictionary(name,code,currentPageTemp,pageSizeTemp);
            if(sysDictionaryEntityList.size()>0){
                for(int i = 0; i < sysDictionaryEntityList.size();i ++){
                    DictionaryDTO dictionaryDTO = new DictionaryDTO();
                    dictionaryDTO.setDictionaryEntity(sysDictionaryEntityList.get(i));
                    dictionaryDTO.setNum(i+1+(currentPageTemp-1)*pageSizeTemp);
                    list.add(dictionaryDTO);
                }
                int count = dictionaryService.getCount(name,code);
                dictionaryResultDTO.setList(list);
                dictionaryResultDTO.setCount(count);
            }
            return Result.SUCCESS(dictionaryResultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PostMapping(value = "/getDictionaryByCode/{code}")
    public Result getDictionaryByCode(@PathVariable("code")String code) {
        try {
            return Result.SUCCESS(dictionaryService.getDictionaryByCode(code));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteDictionary/{id}")
    public Result deleteDictionary(@PathVariable String id) {
        try{
            Optional<SysDictionaryEntity> bean = dictionaryService.findById(id);
            if (bean.isPresent()) {
                SysDictionaryEntity sysDictionaryEntity = bean.get();
                sysDictionaryEntity.setGmtModified(new java.sql.Timestamp(new Date().getTime()));
                sysDictionaryEntity.setIsDeleted(1);
                SysDictionaryEntity dictionaryEntity = dictionaryService.updateDictionary(sysDictionaryEntity);
                return Result.SUCCESS(dictionaryEntity);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findDictionaryById/{id}")
    public Result findDictionaryById(@PathVariable String id) {
        try{
            Optional<SysDictionaryEntity> bean = dictionaryService.findById(id);
            if (bean.isPresent()) {
                SysDictionaryEntity sysDictionaryEntity = bean.get();
                return Result.SUCCESS(sysDictionaryEntity);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addDictionary")
    public Result addDictionary(@RequestBody SysDictionaryEntity sysDictionaryEntity) {
        try{
            return Result.SUCCESS(dictionaryService.addDictionary(sysDictionaryEntity));
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findDictionaryItemByDicId")
    public Result findDictionaryItemByDicId(@RequestParam("id")String id,@RequestParam("itemText")String itemText, @RequestParam("status")String status,@RequestParam("currentPage")String currentPage, @RequestParam("pageSize")String pageSize) {
        int temp = 0;
        int currentPageTemp = 0;
        if(currentPage != "" && currentPage != null){
            currentPageTemp = Integer.parseInt(currentPage);
        }
        int pageSizeTemp = 0;
        if(pageSize != "" && pageSize != null){
            pageSizeTemp = Integer.parseInt(pageSize);
        }
        try{
            if(status != "" && status != null){
                temp = Integer.parseInt(status);
            }else{
                temp = -1;
            }
            List<SysDictionaryItemEntity> list = dictionaryService.findByDictId(id,itemText,temp,currentPageTemp,pageSizeTemp);
            int count = dictionaryService.getDictionaryItemCount(id,itemText,temp);
            DictionaryItemResultDTO dictionaryItemResultDTO = new DictionaryItemResultDTO();
            dictionaryItemResultDTO.setCount(count);
            dictionaryItemResultDTO.setList(list);
            return Result.SUCCESS(dictionaryItemResultDTO);
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteDictionaryItem/{id}")
    public Result deleteDictionaryItem(@PathVariable String id) {
        try{
            Optional<SysDictionaryItemEntity> bean = dictionaryService.findDictionaryItemById(id);
            if (bean.isPresent()) {
                SysDictionaryItemEntity sysDictionaryItemEntity = bean.get();
                sysDictionaryItemEntity.setGmtModified(new java.sql.Timestamp(new Date().getTime()));
                sysDictionaryItemEntity.setIsDeleted(1);
                SysDictionaryItemEntity dictionaryItemEntity = dictionaryService.updateDictionaryItem(sysDictionaryItemEntity);
                return Result.SUCCESS(dictionaryItemEntity);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addDictionaryItem")
    public Result addDictionaryItem(@RequestBody SysDictionaryItemEntity sysDictionaryItemEntity) {
        try{
            return Result.SUCCESS(dictionaryService.addDictionaryItem(sysDictionaryItemEntity));
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/findDictionaryItemById/{id}")
    public Result findDictionaryItemById(@PathVariable String id) {
        try{
            Optional<SysDictionaryItemEntity> bean = dictionaryService.findDictionaryItemById(id);
            if (bean.isPresent()) {
                SysDictionaryItemEntity sysDictionaryItemEntity = bean.get();
                return Result.SUCCESS(sysDictionaryItemEntity);
            }else{
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        }catch (Exception e){
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/checkDictName/{dictName}")
    public Result checkDictName(@PathVariable String dictName) {
        try {
            if (StringUtils.isNotBlank(dictName)) {
                SysDictionaryEntity sysDictionaryEntity = dictionaryService.getByDictName(dictName);
                if (sysDictionaryEntity == null) {
                    return Result.SUCCESS(false);
                }
                return Result.SUCCESS(true);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/checkDictCode/{dictCode}")
    public Result checkDictCode(@PathVariable String dictCode) {
        try {
            if (StringUtils.isNotBlank(dictCode)) {
                SysDictionaryEntity sysDictionaryEntity = dictionaryService.getByDictCode(dictCode);
                if (sysDictionaryEntity == null) {
                    return Result.SUCCESS(false);
                }
                return Result.SUCCESS(true);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PostMapping(value = "/checkItemText")
    public Result checkItemText(@RequestParam("itemText")String itemText,@RequestParam("dictId")String dictId) {
        try {
            if (StringUtils.isNotBlank(itemText)) {
                SysDictionaryItemEntity sysDictionaryItemEntity = dictionaryService.getByItemText(itemText,dictId);
                if (sysDictionaryItemEntity == null) {
                    return Result.SUCCESS(false);
                }
                return Result.SUCCESS(true);
            } else {
                return Result.ERROR(ExceptionEnum.PARAMS_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }
}
