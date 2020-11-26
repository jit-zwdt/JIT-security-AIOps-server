package com.jit.server.service.impl;


import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.pojo.MonitorDailyOperationReportEntity;
import com.jit.server.repository.HostRepo;
import com.jit.server.repository.MonitorClaimRepo;
import com.jit.server.repository.MonitorDailyOperationReportRepo;
import com.jit.server.service.DailyOperationReportService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.ExportXlsFileConst;
import com.jit.server.util.PageRequest;
import com.jit.server.util.SeverityEnum;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.request.ZabbixGetProblemParams;
import com.jit.zabbix.client.service.ZabbixProblemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
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

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

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
            LocalDate localDate = LocalDateTime.now().toLocalDate();
            zabbixGetProblemParams.setTime_from(String.valueOf(LocalDateTime.parse(localDate + " 00:00:00", formatter).toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000));
            zabbixGetProblemParams.setTime_till(String.valueOf(LocalDateTime.parse(localDate + " 23:59:59", formatter).toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000));
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
        LocalDate localDate = LocalDateTime.now().withDayOfMonth(1).toLocalDate();
        zabbixGetProblemParams.setTime_from(String.valueOf(LocalDateTime.parse(localDate + " 00:00:00", formatter).toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000));
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
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(localDate + " 00:00:00", formatter);
        LocalDateTime dateTimeTo = LocalDateTime.parse(localDate + " 23:59:59", formatter);
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
        LocalDate dateFrom = LocalDateTime.now().withDayOfMonth(1).toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(dateFrom + " 00:00:00", formatter);
        LocalDate dateTo = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeTo = LocalDateTime.parse(dateTo + " 23:59:59", formatter);
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
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(localDate + " 00:00:00", formatter);
        LocalDateTime dateTimeTo = LocalDateTime.parse(localDate + " 23:59:59", formatter);
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
        LocalDate dateFrom = LocalDateTime.now().withDayOfMonth(1).toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(dateFrom + " 00:00:00", formatter);
        LocalDate dateTo = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeTo = LocalDateTime.parse(dateTo + " 23:59:59", formatter);
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
        LocalDate localDate = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(localDate + " 00:00:00", formatter);
        LocalDateTime dateTimeTo = LocalDateTime.parse(localDate + " 23:59:59", formatter);
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
        LocalDate dateFrom = LocalDateTime.now().withDayOfMonth(1).toLocalDate();
        LocalDateTime dateTimeFrom = LocalDateTime.parse(dateFrom + " 00:00:00", formatter);
        LocalDate dateTo = LocalDateTime.now().toLocalDate();
        LocalDateTime dateTimeTo = LocalDateTime.parse(dateTo + " 23:59:59", formatter);
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
                        LocalDateTime startDateTime = LocalDateTime.parse(date + " 00:00:00", formatter);
                        LocalDateTime endDateTime = LocalDateTime.parse(date + " 23:59:59", formatter);
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

    /**
     * 导出 Xls 表格 数据根据传入的二维数组进行构建
     * @param dataArray 二维数组数据对象
     * @return Xls 表格对象
     */
    @Override
    public HSSFWorkbook exportDailyXls(String[][] dataArray) {
        //添加的数据的条数
        int rowSize = dataArray.length;
        //大标题的名称
        String headName = ExportXlsFileConst.OPERATION_REPORT_HEAD_NAME;
        //转换时间字符串
        String dataStr = formatter.format(LocalDateTime.now());
        //表头
        String[] tableHeader = ExportXlsFileConst.OPERATION_REPORT_TABLE_HEADER;
        //运维人
        String roleName = dataArray[0][0];
        //表的列数
        short cellNumber=(short)tableHeader.length;
        //创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个工作表
        HSSFSheet sheet = workbook.createSheet(headName);
        //设置列宽
        sheet.setColumnWidth(0 , 256*15+184);
        sheet.setColumnWidth(1 , 256*10+184);
        sheet.setColumnWidth(2 , 256*150+184);
        sheet.setColumnWidth(3 , 256*20+184);
        //创建合并的单元格
        CellRangeAddress region = new CellRangeAddress(0 , 0  , 0  , cellNumber - 1);
        //合并单元格
        sheet.addMergedRegion(region);

        //添加有信息标题
        HSSFRow hssfRow = sheet.createRow(0);
        HSSFCell headCell = hssfRow.createCell(0);
        //设置标题
        headCell.setCellValue(headName);

        //创建文本的样式对象
        HSSFCellStyle cellTitleStyle = workbook.createCellStyle();
        //设置文字居中样式
        cellTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        //创建文字样式对象
        HSSFFont font = workbook.createFont();
        //设置字体
        font.setFontName("楷体");
        //设置文字大小
        font.setFontHeightInPoints((short)30);
        //加粗
        font.setBold(true);
        //放入样式对象
        cellTitleStyle.setFont(font);
        //设置文字居中等样式加入对象
        headCell.setCellStyle(cellTitleStyle);

        //添加信息文字
        hssfRow = sheet.createRow(1);
        //创建正常的文字样式
        HSSFCellStyle cellTextStyle = workbook.createCellStyle();
        //设置文字居中
        cellTextStyle.setAlignment(HorizontalAlignment.CENTER);

        //创建运维人
        headCell = hssfRow.createCell(0);
        headCell.setCellValue(ExportXlsFileConst.OPERATION_MAINTENANCE_PERSON + roleName);
        headCell.setCellStyle(cellTextStyle);

        //创建时间
        headCell = hssfRow.createCell(cellNumber - 1);
        headCell.setCellValue(ExportXlsFileConst.TIME_CONST + dataStr);
        headCell.setCellStyle(cellTextStyle);

        // 添加表头行
        hssfRow = sheet.createRow(2);
        //创建正常的文字样式
        cellTextStyle = workbook.createCellStyle();
        //自动换行
        cellTextStyle.setWrapText(true);//自动换行
        //设置文字居中
        cellTextStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置边框
        cellTextStyle.setBorderBottom(BorderStyle.THIN);
        cellTextStyle.setBorderLeft(BorderStyle.THIN);
        cellTextStyle.setBorderRight(BorderStyle.THIN);
        cellTextStyle.setBorderTop(BorderStyle.THIN);
        //创建标题文字样式
        font = workbook.createFont();
        //设置加粗
        font.setBold(true);
        //放入
        cellTextStyle.setFont(font);
        //循环创建表头
        for(int i = 0 ; i < cellNumber ; i++){
            // 添加表头内容
            headCell = hssfRow.createCell(i);
            headCell.setCellValue(tableHeader[i]);
            headCell.setCellStyle(cellTextStyle);
        }

        //创建正常的文字样式
        cellTextStyle = workbook.createCellStyle();
        //自动换行
        cellTextStyle.setWrapText(true);//自动换行
        //设置文字居中
        cellTextStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置文字垂直居中
        cellTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置文字边框
        cellTextStyle.setBorderBottom(BorderStyle.THIN);
        cellTextStyle.setBorderLeft(BorderStyle.THIN);
        cellTextStyle.setBorderRight(BorderStyle.THIN);
        cellTextStyle.setBorderTop(BorderStyle.THIN);

        //创建主文字的样式
        HSSFCellStyle cellMainTextStyle = workbook.createCellStyle();
        //自动换行
        cellMainTextStyle.setWrapText(true);//自动换行
        //设置文字居中
        cellMainTextStyle.setAlignment(HorizontalAlignment.LEFT);
        //设置文字垂直居中
        cellMainTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置文字边框
        cellMainTextStyle.setBorderBottom(BorderStyle.THIN);
        cellMainTextStyle.setBorderLeft(BorderStyle.THIN);
        cellMainTextStyle.setBorderRight(BorderStyle.THIN);
        cellMainTextStyle.setBorderTop(BorderStyle.THIN);
        //添加主表格数据
        for(int i = 1 ; i < rowSize ; i++){
            hssfRow = sheet.createRow(2 + i);
            //添加文字
            for(int b = 0 ; b < dataArray[i].length ; b++){
                // 添加内容
                headCell = hssfRow.createCell(b);
                headCell.setCellValue(dataArray[i][b]);
                if(b == 2){
                    //设置文字样式
                    headCell.setCellStyle(cellMainTextStyle);
                }else {
                    //设置文字样式
                    headCell.setCellStyle(cellTextStyle);
                }
            }
        }
        //添加尾行
        hssfRow = sheet.createRow(2 + 1 + rowSize);
        //设置行高
        hssfRow.setHeight((short)500);
        //创建正常的文字样式
        cellTextStyle = workbook.createCellStyle();
        //自动换行
        cellTextStyle.setWrapText(true);//自动换行
        //设置文字居中
        cellTextStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置上下的边框
        cellTextStyle.setBorderTop(BorderStyle.THIN);
        cellTextStyle.setBorderBottom(BorderStyle.THIN);
        //设置整个行的上下边框
        for(int i = 0 ; i < cellNumber - 1 ; i++){
            headCell = hssfRow.createCell(i);
            headCell.setCellStyle(cellTextStyle);
        }
        //创建正常的文字样式
        cellTextStyle = workbook.createCellStyle();
        //自动换行
        cellTextStyle.setWrapText(true);//自动换行
        //设置文字居中
        cellTextStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellTextStyle.setBorderTop(BorderStyle.THIN);
        cellTextStyle.setBorderBottom(BorderStyle.THIN);
        cellTextStyle.setBorderLeft(BorderStyle.THIN);
        //设置文字
        headCell = hssfRow.createCell(0);
        headCell.setCellValue(ExportXlsFileConst.PERSON_IN_CHARGE);
        headCell.setCellStyle(cellTextStyle);
        //创建正常的文字样式
        cellTextStyle = workbook.createCellStyle();
        //自动换行
        cellTextStyle.setWrapText(true);//自动换行
        //设置文字居中
        cellTextStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellTextStyle.setBorderTop(BorderStyle.THIN);
        cellTextStyle.setBorderBottom(BorderStyle.THIN);
        cellTextStyle.setBorderRight(BorderStyle.THIN);
        //设置文字
        headCell = hssfRow.createCell(cellNumber - 1);
        headCell.setCellValue(ExportXlsFileConst.DATE_CONST);
        headCell.setCellStyle(cellTextStyle);
        //返回 Controller 进行处理
        return workbook;
    }
}
