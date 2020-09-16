package com.jit.server.service;

import com.jit.server.pojo.SysQuartzJobEntity;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface SysQuartzJobService {

    String saveAndScheduleJob(SysQuartzJobEntity sysQuartzJobEntity) throws Exception;

    SysQuartzJobEntity getSysQuartzJobEntityById(String id) throws Exception;

    Page<SysQuartzJobEntity> getQuartzJobs(PageRequest<Map<String, Object>> params) throws Exception;

    boolean stopScheduleJob(String jobKey) throws Exception;
}
