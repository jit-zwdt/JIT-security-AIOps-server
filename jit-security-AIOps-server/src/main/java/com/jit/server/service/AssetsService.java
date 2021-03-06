package com.jit.server.service;

import com.jit.server.dto.MonitorAssetsDTO;
import com.jit.server.pojo.MonitorAssetsEntity;
import com.jit.server.request.AssetsParams;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AssetsService {
    Page<MonitorAssetsDTO> findByCondition(AssetsParams params, int page, int size) throws Exception;

    void addAssets(MonitorAssetsEntity assets) throws Exception;

    void deleteAssets(String id) throws Exception;

    Optional<MonitorAssetsEntity> findByAssetsId(String id) throws Exception;

    void updateAssets(MonitorAssetsEntity assets) throws Exception;

    List<Object> findByConditionInfo() throws Exception;

    List<Object> getHardwareInfo() throws Exception;

    /**
     * 根据查询语句查询条数和总 CPU 大小 , 内存大小 , 硬件大小
     *
     * @return 数据数组
     */
    List<Object[]> getCountAndSum() throws Exception;

    /**
     * 根据传入的 ip 验证 Ip 值
     *
     * @param ip ip 值
     * @return true 代表有这个数据 false 代表没有这个数据
     */
    boolean validateIp(String ip) throws Exception;

    /**
     * 根据传入的 number 验证 number 值 number: 资产编号
     *
     * @param number ip 值
     * @return true 代表有这个数据 false 代表没有这个数据
     */
    boolean validateNumber(String number) throws Exception;

    MonitorAssetsDTO findMonitorAssetByAssetsId(String id) throws Exception;
}
