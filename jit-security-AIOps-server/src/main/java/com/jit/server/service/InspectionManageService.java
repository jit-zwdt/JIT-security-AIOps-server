package com.jit.server.service;

import com.alibaba.fastjson.JSONObject;
import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorAssetsEntity;
import com.jit.server.pojo.MonitorSchemeTimerTaskEntity;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface InspectionManageService {
    List<HostEntity> getHostInfo(String id) throws Exception;
    void createPDF(String jsonresult) throws Exception;

    /**
     * 获取分页的数据 定时任务管理数据 分页  巡检数据
     * @param params 参数对象
     * @return 分页的 MonitorSchemeTimerTaskEntity 集合对象
     */
    Page<MonitorSchemeTimerTaskEntity> getMonitorSchemeTimerTasks(PageRequest<Map<String, Object>> params);

    /**
     * 根据传入的 Json 数据进行构建一个父对象
     * @param jsonresult json 格式的数据
     * @return 添加的 MonitorSchemeTimerTaskEntity 对象
     */
    MonitorSchemeTimerTaskEntity addMonitorSchemeTimerTask(String jsonresult);

    /**
     * 根据传入的 ID 删除数据 如果数据 id 跟其他的子数据关联也会进行删除操作
     * @param id id 主键
     */
    void deleteMonitorSchemeTimerTask(String id);
}
