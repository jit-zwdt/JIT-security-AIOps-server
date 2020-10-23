package com.jit.server.service.impl;


import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.pojo.MonitorDailyOperationReportEntity;
import com.jit.server.repository.HostRepo;
import com.jit.server.repository.MonitorClaimRepo;
import com.jit.server.repository.MonitorDailyOperationReportRepo;
import com.jit.server.service.DailyOperationReportService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.PageRequest;
import com.jit.server.util.SeverityEnum;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.request.ZabbixGetProblemParams;
import com.jit.zabbix.client.service.ZabbixProblemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class DailyOperationReportServiceImpl implements DailyOperationReportService {
    @Autowired
    private ZabbixProblemService zabbixProblemService;

    @Autowired
    private MonitorClaimRepo monitorClaimRepo;

    @Autowired
    private HostRepo hostRepo;

    @Autowired
    private MonitorDailyOperationReportRepo monitorDailyOperationReportRepo;

    @Override
    public List<String> getTheDateNewProblemList(String auth) throws Exception {
        List<String> res = null;
        ZabbixGetProblemParams zabbixGetProblemParams = new ZabbixGetProblemParams();
        List<Object> hostIds = hostRepo.getHostIds();
        if (hostIds != null && !hostIds.isEmpty()) {
            List<String> hostIdList = new ArrayList<>(hostIds.size());
            Object[] host;
            String hostId;
            for (int i = 0; i < hostIds.size(); i++) {
                host = (Object[]) hostIds.get(i);
                hostId = host[0] != null ? host[0].toString() : "";
                hostIdList.add(hostId);
            }
            zabbixGetProblemParams.setHostids(hostIdList);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate localDate = LocalDateTime.now().toLocalDate();
            zabbixGetProblemParams.setTime_from(String.valueOf(LocalDateTime.parse(localDate + " 00:00:00", df).toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000));
            zabbixGetProblemParams.setTime_till(String.valueOf(LocalDateTime.parse(localDate + " 23:59:59", df).toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000));
            zabbixGetProblemParams.setSortFields(Arrays.asList(new String[]{"eventid"}));
            zabbixGetProblemParams.setSortOrder(Arrays.asList(new String[]{"DESC"}));
            List<ZabbixProblemDTO> zabbixProblemDTOList = zabbixProblemService.get(zabbixGetProblemParams, auth);
            if (zabbixProblemDTOList != null && !zabbixProblemDTOList.isEmpty()) {
                res = new ArrayList<>();
                for (ZabbixProblemDTO zabbixProblemDTO : zabbixProblemDTOList) {
                    res.add("主机名：" + zabbixProblemDTO.getHost() + "，严重等级：" + SeverityEnum.fromValue(zabbixProblemDTO.getSeverity().getValue()).name() + "，问题描述：" + zabbixProblemDTO.getName());
                }
            }
        }
        return res;
    }

    @Override
    public List<String> getTheMonthNewProblemList(String auth) throws Exception {
        List<String> res = null;
        ZabbixGetProblemParams zabbixGetProblemParams = new ZabbixGetProblemParams();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDateTime.now().withDayOfMonth(1).toLocalDate();
        zabbixGetProblemParams.setTime_from(String.valueOf(LocalDateTime.parse(localDate + " 00:00:00", df).toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000));
        zabbixGetProblemParams.setSortFields(Arrays.asList(new String[]{"eventid"}));
        zabbixGetProblemParams.setSortOrder(Arrays.asList(new String[]{"DESC"}));
        List<ZabbixProblemDTO> zabbixProblemDTOList = zabbixProblemService.get(zabbixGetProblemParams, auth);
        if (zabbixProblemDTOList != null && !zabbixProblemDTOList.isEmpty()) {
            res = new ArrayList<>();
            for (ZabbixProblemDTO zabbixProblemDTO : zabbixProblemDTOList) {
                res.add("主机名：" + zabbixProblemDTO.getHost() + "，严重等级：" + SeverityEnum.fromValue(zabbixProblemDTO.getSeverity().getValue()).name() + "，问题描述：" + zabbixProblemDTO.getName());
            }
        }
        return res;
    }

    @Override
    public List<String> getTheDateClaimedProblemList(String auth) throws Exception {
        List<String> res = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(localDate + " 00:00:00", df);
        LocalDateTime dateTimeTo = LocalDateTime.parse(localDate + " 23:59:59", df);
        List<MonitorClaimEntity> monitorClaimEntityList = monitorClaimRepo.getClaimedMonitorClaimEntityByDate(dateTimeFrom, dateTimeTo);
        if (monitorClaimEntityList != null && !monitorClaimEntityList.isEmpty()) {
            res = new ArrayList<>();
            for (MonitorClaimEntity monitorClaimEntity : monitorClaimEntityList) {
                res.add("主机名：" + monitorClaimEntity.getHostName() + "，严重等级：" + SeverityEnum.fromValue(Integer.parseInt(monitorClaimEntity.getSeverity())).name() + "，问题描述：" + monitorClaimEntity.getProblemName());
            }
        }
        return res;
    }

    @Override
    public List<String> getTheMonthClaimedProblemList(String auth) throws Exception {
        List<String> res = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate dateFrom = LocalDateTime.now().withDayOfMonth(1).toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(dateFrom + " 00:00:00", df);
        LocalDate dateTo = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeTo = LocalDateTime.parse(dateTo + " 23:59:59", df);
        List<MonitorClaimEntity> monitorClaimEntityList = monitorClaimRepo.getClaimedMonitorClaimEntityByDate(dateTimeFrom, dateTimeTo);
        if (monitorClaimEntityList != null && !monitorClaimEntityList.isEmpty()) {
            res = new ArrayList<>();
            for (MonitorClaimEntity monitorClaimEntity : monitorClaimEntityList) {
                res.add("主机名：" + monitorClaimEntity.getHostName() + "，严重等级：" + SeverityEnum.fromValue(Integer.parseInt(monitorClaimEntity.getSeverity())).name() + "，问题描述：" + monitorClaimEntity.getProblemName());
            }
        }
        return res;
    }

    @Override
    public List<String> getTheDateProcessingProblemList(String auth) throws Exception {
        List<String> res = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(localDate + " 00:00:00", df);
        LocalDateTime dateTimeTo = LocalDateTime.parse(localDate + " 23:59:59", df);
        List<MonitorClaimEntity> monitorClaimEntityList = monitorClaimRepo.getProcessingMonitorClaimEntityByDate(dateTimeFrom, dateTimeTo);
        if (monitorClaimEntityList != null && !monitorClaimEntityList.isEmpty()) {
            res = new ArrayList<>();
            for (MonitorClaimEntity monitorClaimEntity : monitorClaimEntityList) {
                res.add("主机名：" + monitorClaimEntity.getHostName() + "，严重等级：" + SeverityEnum.fromValue(Integer.parseInt(monitorClaimEntity.getSeverity())).name() + "，问题描述：" + monitorClaimEntity.getProblemName());
            }
        }
        return res;
    }

    @Override
    public List<String> getTheMonthProcessingProblemList(String auth) throws Exception {
        List<String> res = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate dateFrom = LocalDateTime.now().withDayOfMonth(1).toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(dateFrom + " 00:00:00", df);
        LocalDate dateTo = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeTo = LocalDateTime.parse(dateTo + " 23:59:59", df);
        List<MonitorClaimEntity> monitorClaimEntityList = monitorClaimRepo.getProcessingMonitorClaimEntityByDate(dateTimeFrom, dateTimeTo);
        if (monitorClaimEntityList != null && !monitorClaimEntityList.isEmpty()) {
            res = new ArrayList<>();
            for (MonitorClaimEntity monitorClaimEntity : monitorClaimEntityList) {
                res.add("主机名：" + monitorClaimEntity.getHostName() + "，严重等级：" + SeverityEnum.fromValue(Integer.parseInt(monitorClaimEntity.getSeverity())).name() + "，问题描述：" + monitorClaimEntity.getProblemName());
            }
        }
        return res;
    }

    @Override
    public List<String> getTheDateSolvedProblemList(String auth) throws Exception {
        List<String> res = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(localDate + " 00:00:00", df);
        LocalDateTime dateTimeTo = LocalDateTime.parse(localDate + " 23:59:59", df);
        List<MonitorClaimEntity> monitorClaimEntityList = monitorClaimRepo.getSolvedMonitorClaimEntityByDate(dateTimeFrom, dateTimeTo);
        if (monitorClaimEntityList != null && !monitorClaimEntityList.isEmpty()) {
            res = new ArrayList<>();
            for (MonitorClaimEntity monitorClaimEntity : monitorClaimEntityList) {
                res.add("主机名：" + monitorClaimEntity.getHostName() + "，严重等级：" + SeverityEnum.fromValue(Integer.parseInt(monitorClaimEntity.getSeverity())).name() + "，问题描述：" + monitorClaimEntity.getProblemName());
            }
        }
        return res;
    }

    @Override
    public List<String> getTheMonthSolvedProblemList(String auth) throws Exception {
        List<String> res = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate dateFrom = LocalDateTime.now().withDayOfMonth(1).toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(dateFrom + " 00:00:00", df);
        LocalDate dateTo = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeTo = LocalDateTime.parse(dateTo + " 23:59:59", df);
        List<MonitorClaimEntity> monitorClaimEntityList = monitorClaimRepo.getSolvedMonitorClaimEntityByDate(dateTimeFrom, dateTimeTo);
        if (monitorClaimEntityList != null && !monitorClaimEntityList.isEmpty()) {
            res = new ArrayList<>();
            for (MonitorClaimEntity monitorClaimEntity : monitorClaimEntityList) {
                res.add("主机名：" + monitorClaimEntity.getHostName() + "，严重等级：" + SeverityEnum.fromValue(Integer.parseInt(monitorClaimEntity.getSeverity())).name() + "，问题描述：" + monitorClaimEntity.getProblemName());
            }
        }
        return res;
    }

    @Override
    public MonitorDailyOperationReportEntity addDailyOperationReport(MonitorDailyOperationReportEntity monitorDailyOperationReportEntity) throws Exception {
        return monitorDailyOperationReportRepo.saveAndFlush(monitorDailyOperationReportEntity);
    }

    @Override
    public Page<MonitorDailyOperationReportEntity> getDailyOperationReports(PageRequest<Map<String, String>> params) throws Exception {
        Map<String, String> param = params.getParam();
        if (param != null) {
            //条件
            Specification<MonitorDailyOperationReportEntity> spec = new Specification<MonitorDailyOperationReportEntity>() {
                @Override
                public Predicate toPredicate(Root<MonitorDailyOperationReportEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> list = new ArrayList<Predicate>();
                    list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));

                    if (StringUtils.isNotBlank(param.get("queryDate"))) {
                        String date = param.get("queryDate");
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime startDateTime = LocalDateTime.parse(date + " 00:00:00", df);
                        LocalDateTime endDateTime = LocalDateTime.parse(date + " 23:59:59", df);
                        list.add(cb.greaterThanOrEqualTo(root.get("gmtCreate"), startDateTime));
                        list.add(cb.lessThanOrEqualTo(root.get("gmtCreate"), endDateTime));
                    }
                    Predicate[] arr = new Predicate[list.size()];
                    return cb.and(list.toArray(arr));
                }
            };
            //排序的定义
            List<Map<String, String>> orders = params.getOrders();
            List<Sort.Order> orderList = new ArrayList<>();
            orderList.add(new Sort.Order(Sort.Direction.ASC, "gmtCreate"));
            Sort sort = Sort.by(orderList);
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(params.getPage() - 1, params.getSize(), sort);
            return this.monitorDailyOperationReportRepo.findAll(spec, pageable);
        }
        return null;
    }


    @Override
    public MonitorDailyOperationReportEntity getDailyOperationReportById(String id) throws Exception {
        return monitorDailyOperationReportRepo.findByIdAndIsDeleted(id,ConstUtil.IS_NOT_DELETED);
    }
}
