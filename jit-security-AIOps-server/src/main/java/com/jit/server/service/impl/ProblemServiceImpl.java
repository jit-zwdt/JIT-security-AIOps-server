package com.jit.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.jit.server.dto.ProblemClaimDTO;
import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.pojo.MonitorClaimEntity;
import com.jit.server.pojo.SysUserEntity;
import com.jit.server.repository.HostRepo;
import com.jit.server.repository.MonitorClaimRepo;
import com.jit.server.repository.SysUserRepo;
import com.jit.server.request.ProblemClaimParams;
import com.jit.server.request.ProblemParams;
import com.jit.server.service.ProblemService;
import com.jit.server.service.ZabbixAuthService;
import com.jit.server.util.ConstUtil;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProblemServiceImpl implements ProblemService {

    public static final String EXTEND = "extend";

    @Autowired
    private MonitorClaimRepo monitorClaimRepo;

    @Autowired
    private HostRepo hostRepo;

    @Autowired
    private ZabbixProblemService zabbixProblemService;

    @Autowired
    private ZabbixAuthService zabbixAuthService;

    @Autowired
    private SysUserRepo sysUserRepo;

    @Override
    public List<ZabbixProblemDTO> findByCondition(ProblemParams params, String authToken) throws Exception {
        if (StringUtils.isBlank(authToken)) {
            return null;
        }

        ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();

        // set severity
        if (params.getSeverity() != null) {
            Map<String, Object> mapFilter = new HashMap();
            mapFilter.put("severity", params.getSeverity());
            params_pro.setFilter(mapFilter);
        }

        // set hostId
        if (params.getHostIds() != null) {
            params_pro.setHostids(Arrays.asList(params.getHostIds().split(",")));
        }

        // set timeFrom
        if (params.getTimeFrom() != null && !params.getTimeFrom().equals("NaN")) {
            params_pro.setTime_from(params.getTimeFrom());
        }

        // set timeTill
        if (params.getTimeTill() != null && !params.getTimeTill().equals("NaN")) {
            params_pro.setTime_till(params.getTimeTill());
        }

        // set name
        if (params.getName() != null && params.getName().length() > 0) {
            Map<String, Object> mapSearch = new HashMap();
            mapSearch.put("name", params.getName());
            params_pro.setSearch(mapSearch);
        }
        params_pro.setSortFields(Arrays.asList(new String[]{"eventid"}));
        params_pro.setSortOrder(Arrays.asList(new String[]{"DESC"}));

        return zabbixProblemService.get(params_pro, authToken);
    }

    @Override
    public List<ProblemHostDTO> findProblemHost(ProblemParams params, String auth) throws Exception {
        // get host info
        List<Object> hostInfo = hostRepo.getHostIdsAndIp();

        if (hostInfo != null && !hostInfo.isEmpty()) {
            List<String> hostIds = new ArrayList<>(hostInfo.size());
            // create a map to store host information, using host id as key
            Map<String, Object[]> mapHostInfo = new HashMap<>(hostInfo.size());
            String hostid;
            Object[] host;
            for (int i = 0; i < hostInfo.size(); i++) {
                host = (Object[]) hostInfo.get(i);
                hostid = host[0] != null ? host[0].toString() : "";
                mapHostInfo.put(hostid, host);
                hostIds.add(hostid);
            }

            // get problems
            List<ProblemHostDTO> problemHostDTOs = new ArrayList<>();
            if (StringUtils.isBlank(params.getHostIds())) {
                params.setHostIds(StringUtils.join(hostIds, ","));
            }
            // for each hostId, find problem
            List<ZabbixProblemDTO> problems = findByCondition(params, auth);
            if (problems != null) {
                for (ZabbixProblemDTO problem : problems) {
                    ProblemHostDTO problemHostDTO = new ProblemHostDTO();
                    problemHostDTO.setZabbixProblemDTO(problem);
                    Object[] obj = mapHostInfo.get(problem.getHostid());
                    problemHostDTO.setHostId(obj[0].toString());
                    problemHostDTO.setHostName(obj[1].toString());
                    problemHostDTO.setIp(obj[2].toString());
                    problemHostDTOs.add(problemHostDTO);
                }
            }
            return problemHostDTOs;
        } else {
            return null;
        }
    }

    @Override
    public List<ProblemClaimDTO> findBySeverityLevel(ProblemClaimParams params, String auth) throws Exception {
        List<ProblemClaimDTO> list = new ArrayList<>();
        String claimType = params.getClaimType();
        if (claimType == null || "".equals(claimType)) {
            claimType = "-1";
        }
        List<Object> hostInfo = hostRepo.getHostIdsAndIp();
        if (hostInfo != null && !hostInfo.isEmpty()) {
            List<String> hostIds = new ArrayList<>(hostInfo.size());
            String hostid;
            Object[] host;
            for (int i = 0; i < hostInfo.size(); i++) {
                host = (Object[]) hostInfo.get(i);
                hostid = host[0] != null ? host[0].toString() : "";
                hostIds.add(hostid);
            }

            ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();
            List<Integer> severities = params.getSeverities();
            if (severities != null && !severities.isEmpty()) {
                params_pro.setSeverities(severities);
                params_pro.setHostids(hostIds);
                params_pro.setSortFields(Arrays.asList(new String[]{"eventid"}));
                params_pro.setSortOrder(Arrays.asList(new String[]{"DESC"}));
                List<ZabbixProblemDTO> listZ = zabbixProblemService.get(params_pro, auth);
                List<ProblemClaimDTO> problemClaimDTOS = new ArrayList<>();
                for (ZabbixProblemDTO zabbixProblemDTO : listZ) {
                    if ("0".equals(claimType)) {
                        ProblemClaimDTO problemClaimDTO = new ProblemClaimDTO();
                        problemClaimDTO.setZabbixProblemDTO(zabbixProblemDTO);
                        MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
                        if (temp == null) {
                            problemClaimDTO.setIsClaim(0);
                            problemClaimDTO.setClaimUser("-");
                            problemClaimDTOS.add(problemClaimDTO);
                        }
                    } else if ("1".equals(claimType)) {
                        ProblemClaimDTO problemClaimDTO = new ProblemClaimDTO();
                        problemClaimDTO.setZabbixProblemDTO(zabbixProblemDTO);
                        MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
                        if (temp != null) {
                            problemClaimDTO.setIsClaim(temp.getIsClaim());
                            problemClaimDTO.setClaimUser(sysUserRepo.findNameById(temp.getClaimUserId()));
                            problemClaimDTOS.add(problemClaimDTO);
                        }
                    } else {
                        ProblemClaimDTO problemClaimDTO = new ProblemClaimDTO();
                        problemClaimDTO.setZabbixProblemDTO(zabbixProblemDTO);
                        MonitorClaimEntity temp = monitorClaimRepo.getMonitorClaimEntityById(zabbixProblemDTO.getId());
                        if (temp != null) {
                            problemClaimDTO.setIsClaim(temp.getIsClaim());
                            problemClaimDTO.setClaimUser(sysUserRepo.findNameById(temp.getClaimUserId()));
                        } else {
                            problemClaimDTO.setClaimUser("-");
                        }
                        problemClaimDTOS.add(problemClaimDTO);
                    }
                }
                list.addAll(problemClaimDTOS);
            }
            return list;
        } else {
            return null;
        }
    }

    @Override
    public void addCalim(MonitorClaimEntity monitorClaimEntity) throws Exception {
        monitorClaimRepo.save(monitorClaimEntity);
    }

    @Override
    public List<MonitorClaimEntity> findClaimByUser(String problemName, int resolveType) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUserEntity user = sysUserRepo.findByUsername(username);
        List<MonitorClaimEntity> list = monitorClaimRepo.findClaimByUser(user.getId(), "%"+problemName+"%", resolveType);
        return list;
    }

    @Override
    public void updateClaimAfterRegister(MonitorClaimEntity monitorClaimEntity) {
        if (monitorClaimEntity.getIsResolve() == ConstUtil.RESOLVED) {
            monitorClaimRepo.updateClaimAfterRegister(monitorClaimEntity.getId(), monitorClaimEntity.getIsRegister(), monitorClaimEntity.getIsResolve(), monitorClaimEntity.getProblemHandleTime(), LocalDateTime.now());
        } else {
            monitorClaimRepo.updateClaimAfterRegister(monitorClaimEntity.getId(), monitorClaimEntity.getIsRegister(), monitorClaimEntity.getIsResolve(), monitorClaimEntity.getProblemHandleTime());
        }
    }

    @Override
    public MonitorClaimEntity findByProblemId(String problemId) {
        return monitorClaimRepo.getMonitorClaimEntityById(problemId);
    }

    @Override
    public List<ZabbixProblemDTO> getAlertdata(ProblemParams params, String auth) throws Exception {
        ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();

        // set severity
        if (params.getSeverity() != null) {
            Map<String, Object> mapFilter = new HashMap();
            mapFilter.put("severity", params.getSeverity());
            params_pro.setFilter(mapFilter);
        }

        // set hostId
        if (params.getHostIds() != null) {
            params_pro.setHostids(Arrays.asList(params.getHostIds().split(",")));
        }

        // set timeFrom
        if (params.getTimeFrom() != null && !params.getTimeFrom().equals("NaN")) {
            params_pro.setTime_from(params.getTimeFrom());
        }

        // set timeTill
        if (params.getTimeTill() != null && !params.getTimeTill().equals("NaN")) {
            params_pro.setTime_till(params.getTimeTill());
        }

        // set name
        if (params.getName() != null && params.getName().length() > 0) {
            Map<String, Object> mapSearch = new HashMap();
            mapSearch.put("name", params.getName());
            params_pro.setSearch(mapSearch);
        }

        return zabbixProblemService.get(params_pro, auth);
    }

    @Override
    public List<MonitorClaimEntity> findAllClaim() {
        return monitorClaimRepo.findAll();
    }

    @Override
    public List<MonitorClaimEntity> findByIsResolve() {
        return monitorClaimRepo.findByIsResolve(1);
    }

    @Override
    public List<MonitorClaimEntity> findByIsResolveAndProblemName(String problemName) {
        return monitorClaimRepo.findByIsResolveAndProblemName(1, problemName);
    }

    @Override
    public List<MonitorClaimEntity> findByIsResolveAndProblemName(String problemName, String resolveTimeStart, String resolveTimeEnd) {
        LocalDateTime resolveTimeStart1 = LocalDateTime.parse(resolveTimeStart + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime resolveTimeEnd1 = LocalDateTime.parse(resolveTimeEnd + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return monitorClaimRepo.findByIsResolveAndProblemName(1, problemName, resolveTimeStart1, resolveTimeEnd1);
    }

    @Override
    public List<MonitorClaimEntity> findByIsResolve(String resolveTimeStart, String resolveTimeEnd) throws Exception {
        LocalDateTime resolveTimeStart1 = LocalDateTime.parse(resolveTimeStart + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime resolveTimeEnd1 = LocalDateTime.parse(resolveTimeEnd + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return monitorClaimRepo.findByIsResolve(resolveTimeStart1, resolveTimeEnd1);
    }

    @Override
    public List<ZabbixProblemDTO> findProblemById(String[] params,String auth) throws Exception {
        ZabbixGetProblemParams params_pro = new ZabbixGetProblemParams();
        if (params!= null ) {
            Map<String, Object> mapFilter = new HashMap();
            mapFilter.put("objectid", params);
            params_pro.setFilter(mapFilter);
        }
        return zabbixProblemService.get(params_pro, auth);
    }

    /**
     * 根据传入的故障解决数据构建 xls 文件
     * @param dataArray 故障解决数据 json 格式的字符串
     * @return xls 文件对象
     */
    @Override
    public HSSFWorkbook downLoadFailureToSolve(String[][] dataArray) {
        //添加的数据的条数
        int rowSize = dataArray.length;
        //大标题的名称
        String headName = "故障解决统计报表";
        //安全的时间转换类对象的声明
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //转换时间字符串
        String dataStr = formatter.format(LocalDateTime.now());
        //表头
        String[] tableHeader = {"序号","故障解决日期","故障名称","故障类型","处理角色","处理人","故障原因","处理过程","处理方式","故障持续时间","故障处理时长"};
        //表的列数
        short cellNumber = (short)tableHeader.length;
        //创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个工作表
        HSSFSheet sheet = workbook.createSheet(headName);
        //设置列宽
        sheet.setColumnWidth(0 , 256*10+184);
        sheet.setColumnWidth(1 , 256*25+184);
        sheet.setColumnWidth(2 , 256*40+184);
        sheet.setColumnWidth(3 , 256*15+184);
        sheet.setColumnWidth(4 , 256*15+184);
        sheet.setColumnWidth(5 , 256*15+184);
        sheet.setColumnWidth(6 , 256*25+184);
        sheet.setColumnWidth(7 , 256*25+184);
        sheet.setColumnWidth(8 , 256*25+184);
        sheet.setColumnWidth(9 , 256*25+184);
        sheet.setColumnWidth(10 , 256*25+184);
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

        //创建时间
        headCell = hssfRow.createCell(cellNumber - 1);
        headCell.setCellValue("日期:" + dataStr);
        headCell.setCellStyle(cellTextStyle);

        // 添加表头行
        hssfRow = sheet.createRow(2);
        //创建正常的文字样式
        cellTextStyle = workbook.createCellStyle();
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
        //设置文字居中
        cellTextStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置文字垂直居中
        cellTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置文字边框
        cellTextStyle.setBorderBottom(BorderStyle.THIN);
        cellTextStyle.setBorderLeft(BorderStyle.THIN);
        cellTextStyle.setBorderRight(BorderStyle.THIN);
        cellTextStyle.setBorderTop(BorderStyle.THIN);
        //添加主表格数据
        for(int i = 0 ; i < rowSize ; i++){
            hssfRow = sheet.createRow(2 + 1 + i);
            //添加文字
            for(int b = 0 ; b < dataArray[i].length ; b++){
                // 添加内容
                headCell = hssfRow.createCell(b);
                headCell.setCellValue(dataArray[i][b]);
                headCell.setCellStyle(cellTextStyle);
            }
        }
        //返回 Controller 进行处理
        return workbook;
    }
}
