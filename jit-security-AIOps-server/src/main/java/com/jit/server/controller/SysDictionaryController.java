package com.jit.server.controller;


import com.jit.server.annotation.AutoLog;
import com.jit.server.dto.*;
import com.jit.server.exception.ExceptionEnum;
import com.jit.server.pojo.SysDictionaryEntity;
import com.jit.server.pojo.SysDictionaryItemEntity;
import com.jit.server.service.DictionaryService;
import com.jit.server.util.ConstLogUtil;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @ResponseBody
    @PostMapping(value = "/getDictionarys")
    @AutoLog(value = "字典管理-查询", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getDictionarys(@RequestParam("name") String name, @RequestParam("code") String code, @RequestParam("currentPage") String currentPage, @RequestParam("pageSize") String pageSize) {
        int currentPageTemp = 0;
        if (currentPage != "" && currentPage != null) {
            currentPageTemp = Integer.parseInt(currentPage);
        }
        int pageSizeTemp = 0;
        if (pageSize != "" && pageSize != null) {
            pageSizeTemp = Integer.parseInt(pageSize);
        }
        try {
            List<DictionaryDTO> list = new ArrayList<>();
            DictionaryResultDTO dictionaryResultDTO = new DictionaryResultDTO();
            Page<SysDictionaryDTO> pageResult = dictionaryService.getDictionary(name, code, currentPageTemp, pageSizeTemp);
            if (pageResult.getContent().size() > 0) {
                for (int i = 0; i < pageResult.getContent().size(); i++) {
                    DictionaryDTO dictionaryDTO = new DictionaryDTO();
                    dictionaryDTO.setSysDictionaryDTO(pageResult.getContent().get(i));
                    dictionaryDTO.setNum(i + 1 + (currentPageTemp - 1) * pageSizeTemp);
                    list.add(dictionaryDTO);
                }
                dictionaryResultDTO.setList(list);
                dictionaryResultDTO.setCount((int) pageResult.getTotalElements());
            }
            return Result.SUCCESS(dictionaryResultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @PostMapping(value = "/getDictionaryByCode/{code}")
    public Result getDictionaryByCode(@PathVariable("code") String code) {
        try {
            return Result.SUCCESS(dictionaryService.getDictionaryItemsByCode(code));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getDictByCode/{code}")
    public Result getDictByCode(@PathVariable("code") String code) {
        try {
            return Result.SUCCESS(dictionaryService.getDictByCode(code));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ExceptionEnum.QUERY_DATA_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteDictionary/{id}")
    @AutoLog(value = "字典管理-删除", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result deleteDictionary(@PathVariable String id) {
        try {
            Optional<SysDictionaryEntity> bean = dictionaryService.findById(id);
            if (bean.isPresent()) {
                SysDictionaryEntity sysDictionaryEntity = bean.get();
                sysDictionaryEntity.setGmtModified(LocalDateTime.now());
                sysDictionaryEntity.setIsDeleted(ConstUtil.IS_DELETED);
                dictionaryService.updateDictionary(sysDictionaryEntity);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getDictionary/{id}")
    public Result getDictionary(@PathVariable String id) {
        try {
            SysDictionaryDTO sysDictionaryDTO = dictionaryService.findSysDictionaryById(id);
            if (sysDictionaryDTO != null) {
                return Result.SUCCESS(sysDictionaryDTO);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addDictionary")
    @AutoLog(value = "字典管理-新增", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addDictionary(@RequestBody SysDictionaryEntity sysDictionaryEntity) {
        try {
            sysDictionaryEntity.setDictCode(sysDictionaryEntity.getDictCode().trim().toLowerCase());
            dictionaryService.addDictionary(sysDictionaryEntity);
            return Result.SUCCESS(null);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateDictionary")
    @AutoLog(value = "字典管理-编辑", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateDictionary(@RequestBody SysDictionaryDTO sysDictionaryDTO) {
        try {
            Optional<SysDictionaryEntity> bean = dictionaryService.findById(sysDictionaryDTO.getId());
            if (bean.isPresent()) {
                SysDictionaryEntity sysDictionaryEntity = bean.get();
                BeanUtils.copyProperties(sysDictionaryDTO, sysDictionaryEntity);
                sysDictionaryEntity.setDictCode(sysDictionaryDTO.getDictCode().trim().toLowerCase());
                dictionaryService.addDictionary(sysDictionaryEntity);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getDictionaryItemByDicId")
    @AutoLog(value = "字典管理-查询子项", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result getDictionaryItemByDicId(@RequestParam("id") String id, @RequestParam("itemText") String itemText, @RequestParam("status") String status, @RequestParam("currentPage") String currentPage, @RequestParam("pageSize") String pageSize) {
        int temp = 0;
        int currentPageTemp = 0;
        if (currentPage != "" && currentPage != null) {
            currentPageTemp = Integer.parseInt(currentPage);
        }
        int pageSizeTemp = 0;
        if (pageSize != "" && pageSize != null) {
            pageSizeTemp = Integer.parseInt(pageSize);
        }
        try {
            if (status != "" && status != null) {
                temp = Integer.parseInt(status);
            } else {
                temp = -1;
            }
            Page<SysDictionaryItemDTO> list = dictionaryService.findByDictId(id, itemText, temp, currentPageTemp, pageSizeTemp);
            DictionaryItemResultDTO dictionaryItemResultDTO = new DictionaryItemResultDTO();
            dictionaryItemResultDTO.setCount((int) list.getTotalElements());
            dictionaryItemResultDTO.setList(list.getContent());
            return Result.SUCCESS(dictionaryItemResultDTO);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @DeleteMapping("/deleteDictionaryItem/{id}")
    @AutoLog(value = "字典管理-删除子项", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result deleteDictionaryItem(@PathVariable String id) {
        try {
            Optional<SysDictionaryItemEntity> bean = dictionaryService.findDictionaryItemById(id);
            if (bean.isPresent()) {
                SysDictionaryItemEntity sysDictionaryItemEntity = bean.get();
                sysDictionaryItemEntity.setGmtModified(LocalDateTime.now());
                sysDictionaryItemEntity.setIsDeleted(ConstUtil.IS_DELETED);
                SysDictionaryItemEntity dictionaryItemEntity = dictionaryService.updateDictionaryItem(sysDictionaryItemEntity);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/addDictionaryItem")
    @AutoLog(value = "字典管理-新增子项", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result addDictionaryItem(@RequestBody SysDictionaryItemEntity sysDictionaryItemEntity) {
        try {
            dictionaryService.addDictionaryItem(sysDictionaryItemEntity);
            return Result.SUCCESS(null);
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/updateDictionaryItem")
    @AutoLog(value = "字典管理-编辑子项", logType = ConstLogUtil.LOG_TYPE_OPERATION)
    public Result updateDictionaryItem(@RequestBody SysDictionaryItemDTO sysDictionaryItemDTO) {
        try {
            Optional<SysDictionaryItemEntity> bean = dictionaryService.findDictionaryItemById(sysDictionaryItemDTO.getId());
            if (bean.isPresent()) {
                SysDictionaryItemEntity sysDictionaryItemEntity = bean.get();
                BeanUtils.copyProperties(sysDictionaryItemDTO, sysDictionaryItemEntity);
                dictionaryService.addDictionaryItem(sysDictionaryItemEntity);
                return Result.SUCCESS(null);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
            return Result.ERROR(ExceptionEnum.INNTER_EXCEPTION);
        }
    }

    @PostMapping("/getDictionaryItem/{id}")
    public Result getDictionaryItem(@PathVariable String id) {
        try {
            SysDictionaryItemDTO sysDictionaryItemDTO = dictionaryService.findDictItemById(id);
            if (sysDictionaryItemDTO != null) {
                return Result.SUCCESS(sysDictionaryItemDTO);
            } else {
                return Result.ERROR(ExceptionEnum.RESULT_NULL_EXCEPTION);
            }
        } catch (Exception e) {
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
                SysDictionaryEntity sysDictionaryEntity = dictionaryService.getByDictCode(dictCode.trim().toLowerCase());
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
    public Result checkItemText(@RequestParam("itemText") String itemText, @RequestParam("dictId") String dictId) {
        try {
            if (StringUtils.isNotBlank(itemText)) {
                SysDictionaryItemEntity sysDictionaryItemEntity = dictionaryService.getByItemText(itemText, dictId);
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
