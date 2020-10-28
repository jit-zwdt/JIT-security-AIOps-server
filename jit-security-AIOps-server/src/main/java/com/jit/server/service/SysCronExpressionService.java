package com.jit.server.service;

import com.jit.server.dto.CronExpressionDTO;
import com.jit.server.pojo.SysCronExpressionEntity;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SysCronExpressionService {

    Page<SysCronExpressionEntity> getCronExpressions(PageRequest<Map<String, Object>> params);

    List<CronExpressionDTO> getCronExpressionObject() throws Exception;

    /**
     * 添加一个时间表达式对象数据
     * @param cronExpression 时间表达式对象
     * @return 添加成功的时间表达式对象
     */
    SysCronExpressionEntity addCronExpression(SysCronExpressionEntity cronExpression);

    /**
     * 根据时间表达式的 ID 删除一条数据
     * @param id 时间表达式的 id
     */
    void delCronExpression(String id);
}
