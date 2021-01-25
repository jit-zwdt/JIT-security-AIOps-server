package com.jit.server.service;

import com.jit.server.dto.CronExpressionDTO;
import com.jit.server.dto.SysCronExpressionDTO;
import com.jit.server.pojo.SysCronExpressionEntity;
import com.jit.server.util.PageRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SysCronExpressionService {

    Page<SysCronExpressionDTO> getCronExpressions(PageRequest<Map<String, Object>> params);

    List<CronExpressionDTO> getCronExpressionObject() throws Exception;

    /**
     * 添加一个时间表达式对象数据
     * @param cronExpression 时间表达式对象
     * @return 添加成功的时间表达式对象
     */
    SysCronExpressionEntity addCronExpression(SysCronExpressionEntity cronExpression);

    /**
     * 根据表达式查询数据
     * @param cronExpression 表达式
     * @return 是否具有这个数据 true 有 false 没有
     */
    boolean checkAddCronExpression(String cronExpression);

    /**
     * 根据表达式的描述查询数据
     * @param cronExpressionDesc 表达式描述查询数据
     * @return 是否具有这个数据 true 有 false 没有
     */
    boolean checkAddCronExpressionDesc(String cronExpressionDesc);

    /**
     * 根据时间表达式的 ID 删除一条数据
     * @param id 时间表达式的 id
     */
    void delCronExpression(String id);

    /**
     * 查询所有的时间表达式
     * @return 时间表达式数据
     */
    List<SysCronExpressionEntity> findAllCronExpression();
}
