package com.jit.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jit.server.config.FtpConfig;
import com.jit.server.dto.MonitorSchemeTimerTaskEntityDto;
import com.jit.server.pojo.HostEntity;
import com.jit.server.pojo.MonitorSchemeTimerTaskEntity;
import com.jit.server.repository.InspectionRepo;
import com.jit.server.service.InspectionManageService;
import com.jit.server.util.ConstUtil;
import com.jit.server.util.FtpClientUtil;
import com.jit.server.util.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
public class InspectionManageServiceImpl implements InspectionManageService {
    @Autowired
    private InspectionRepo inspectionRepo;
    @Autowired
    private FtpConfig ftpConfig;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<HostEntity> getHostInfo(String id) throws Exception {
        try {
            if (StringUtils.isNotEmpty(id)) {
                List<HostEntity> listinfoOne = inspectionRepo.getHostInfoById(id);
                return listinfoOne;
            } else {
                List<HostEntity> listinfoAll = inspectionRepo.getHostInfo();
                return listinfoAll;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createPDF(String jsonresult) throws Exception {
        try {
            String filename = UUID.randomUUID().toString();
            String basePath = getResourceBasePath();
            String resourcePath = new File(basePath, "src/main/java/com/jit/server/file/pdf/" + filename + ".pdf").getAbsolutePath();
            ensureDirectory(resourcePath);
            String filepath = resourcePath;
            makeDocumentdata(filepath, jsonresult);
            //获取创建的 URL
            String url = makepdf(filepath, jsonresult);
            createSchemeTable(url, jsonresult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeDocumentdata(String filepath, String jsonresult) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filepath));
            document.addTitle("巡检报告");
            document.open();
            BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            Font title = new Font(bfChinese, 20, Font.BOLD);
            Font title40 = new Font(bfChinese, 40, Font.BOLD);
            Paragraph docTitle = new Paragraph("巡检报告信息表\n", title);
            docTitle.setAlignment(Element.ALIGN_CENTER);
            docTitle.setSpacingBefore(10);
            document.add(docTitle);
            PdfPTable table = createHeaderTable(jsonresult);
            document.add(table);
            Paragraph blankRow = new Paragraph(18f, " ", title40);
            document.add(blankRow);
            PdfPTable tableChild = createChildTable(jsonresult);
            document.add(tableChild);
            document.add(blankRow);
            PdfPTable tableFooter = createFooterTable();
            document.add(tableFooter);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    public static PdfPTable createHeaderTable(String jsonresult) throws DocumentException, IOException {
        JSONObject jsonObject = JSONObject.parseObject(jsonresult);
        String sumNum = jsonObject.get("sumNum")+"";
        String exSumNum =  jsonObject.get("exSumNum")+"";
        String schemeName =  jsonObject.get("schemeName")+"";

        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 12,Font.NORMAL);

        PdfPTable table = new PdfPTable(4); // 3 columns.
        table.setWidthPercentage(100); // Width 100%
        table.setSpacingBefore(10f); // Space before table
        table.setSpacingAfter(10f); // Space after table
        table.setTotalWidth(500); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        PdfPCell cell;

        cell = new PdfPCell(new Paragraph("巡检计划名称", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph(schemeName, font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        cell.setColspan(3);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("负责单位", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("吉大正元", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        cell.setColspan(3);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("联系人", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("刘峰", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("联系电话", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("15504252525", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("巡检总数", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph(sumNum+"", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("异常数量", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph(exSumNum+"", font));
        cell.setPaddingLeft(10);
        try {
            if (exSumNum != null &&  Integer.valueOf(exSumNum) > 0) {
                cell.setBorderColor(BaseColor.RED);
            }
        }catch(Exception e) {
             e.printStackTrace();
        }

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        return table;
    }

    public static PdfPTable createChildTable(String jsonresult) throws DocumentException, IOException {
        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 12,Font.NORMAL);
        Font font8 = new Font(bfChinese, 8,Font.NORMAL);

        PdfPTable table = new PdfPTable(4); // 3 columns.
        table.setWidthPercentage(100); // Width 100%
        table.setSpacingBefore(10f); // Space before table
        table.setSpacingAfter(10f); // Space after table
        table.setTotalWidth(500); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        int width[] = {5,20,60,20};
        table.setWidths(width);
        PdfPCell cell;

        cell = new PdfPCell(new Paragraph("序号", font));
        cell.setPaddingLeft(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);


        cell = new PdfPCell(new Paragraph("主机名称", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("巡检对象", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("备注", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        JSONObject jsonObject = JSONObject.parseObject(jsonresult);
        String resultData = jsonObject.get("resultData") + "";
        JSONArray infojson = JSONArray.parseArray(resultData);
        for (int i = 0; i < infojson.size(); i++) {
            JSONObject job = infojson.getJSONObject(i);

            String num = job.get("num") + "";
            String hostname = job.get("hostname") + "";
            String description = job.get("description") + "";
            String datainfo = job.get("datainfo") + "";

            cell = new PdfPCell(new Paragraph(num, font8));
            cell.setPaddingLeft(1);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setMinimumHeight(17);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(hostname, font8));
            cell.setPaddingLeft(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setMinimumHeight(17);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(description, font8));
            cell.setPaddingLeft(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setMinimumHeight(17);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(datainfo, font8));
            cell.setPaddingLeft(10);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setMinimumHeight(17);
            table.addCell(cell);
        }

        return table;
    }

    public static PdfPTable createFooterTable() throws DocumentException, IOException {
        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 11,Font.NORMAL);

        PdfPTable table = new PdfPTable(2);
        int width3[] = {100,60};
        table.setWidths(width3);
        PdfPCell cell31 = new PdfPCell(new Paragraph("巡检工程师："+"刘峰",font));
        PdfPCell cell32 = new PdfPCell(new Paragraph("日期："+"2020-09-15",font));
        cell31.setBorder(0);
        cell31.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell32.setBorder(0);
        cell32.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell31);
        table.addCell(cell32);
        return table;
    }

    /**
     * 保证拷贝的文件的目录一定要存在
     *
     * @param filePath
     *            文件路径
     */
    public static void ensureDirectory(String filePath) {
        if (filePath == null) {
            return;
        }
        filePath = replaceSeparator(filePath);
        if (filePath.indexOf("/") != -1) {
            filePath = filePath.substring(0, filePath.lastIndexOf("/"));
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    /**
     * 将符号“\\”和“\”替换成“/”,有时候便于统一的处理路径的分隔符,避免同一个路径出现两个或三种不同的分隔符
     *
     * @param str
     * @return
     */
    public static String replaceSeparator(String str) {
        return str.replace("\\", "/").replace("\\\\", "/");
    }

    /**
     * 获取项目根路径
     *
     * @return
     */
    private static String getResourceBasePath() {
        // 获取跟目录
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (path == null || !path.exists()) {
            path = new File("");
        }

        String pathStr = path.getAbsolutePath();
        pathStr = pathStr.replace("\\target\\classes", "");

        return pathStr;
    }

    public String makepdf(String filepath, String jsonresult) throws Exception {
        if (filepath == null) {
            throw new Exception("pdf路径为空");
        }
        String url = "";
        File file = new File(filepath);
        if (file.exists()) {
            FileInputStream input = null;
            try {
                input = new FileInputStream(file);
                url = makeftp(input, filepath, jsonresult);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    input.close();
                }
                if (file != null) {
                    file.delete();
                }
            }
            return url;
        } else {
            return url;
        }
    }

    public String makeftp(FileInputStream file, String filepath, String jsonresult) throws Exception {
        FTPClient ftp = null;
        String url = "";
        try {
            if (file != null) {
                JSONObject jsonObject = JSONObject.parseObject(jsonresult);
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHH");
                SimpleDateFormat sft = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date();
                String path = "/"+sft.format(date)+"/"+sf.format(date)+"/";
                FtpClientUtil a = new FtpClientUtil();
                ftp = a.getConnectionFTP(ftpConfig.getHostName(), ftpConfig.getPort(), ftpConfig.getUserName(), ftpConfig.getPassWord());
                url = a.uploadFile(ftp, path, filepath, file);
                // 注释掉了本行代码添加了 '/' 的处理
//                url = url.replace("/", "");
                return url;
            } else {
                return url;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return url;
    }

    public void createSchemeTable(String url, String jsonresult) throws Exception {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonresult);
            String scheduleId = jsonObject.get("scheduleId")+"";
            String schemeName = jsonObject.get("schemeName")+"";
            String username = jsonObject.get("username")+"";
            String parentId = jsonObject.get("parentId") + "";
            MonitorSchemeTimerTaskEntity schemeTimerTaskEntity = new MonitorSchemeTimerTaskEntity();
            schemeTimerTaskEntity.setScheduleId(scheduleId);
            schemeTimerTaskEntity.setSchemeName(schemeName);
            schemeTimerTaskEntity.setFtpUrl(url);
            schemeTimerTaskEntity.setGmtCreate(LocalDateTime.now());
            schemeTimerTaskEntity.setCreateBy(username);
            schemeTimerTaskEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
            schemeTimerTaskEntity.setParentId(parentId);
            inspectionRepo.save(schemeTimerTaskEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 获取分页的数据 定时任务管理数据 分页  巡检数据
     * @param params 参数对象
     * @return 分页的 MonitorSchemeTimerTaskEntity 集合对象
     */
    @Override
    public Page<MonitorSchemeTimerTaskEntityDto> getMonitorSchemeTimerTasks(PageRequest<Map<String, Object>> params) throws ParseException {
        Map<String, Object> param = params.getParam();
        String schemeName = param.get("schemeName")!= null ? param.get("schemeName").toString() : "";
        String parentId = param.get("parentId")!= null ? param.get("parentId").toString() : "";
        //"2019-10-12 10:00:00"
        //声明日期格式转换对象
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        //进行字符串转换 LocalDateTime 对象
        LocalDateTime startGmtCreate = param.get("startGmtCreate")!= null ? LocalDateTime.parse(param.get("startGmtCreate").toString() , dateTimeFormatter) : null;
        LocalDateTime endGmtCreate = param.get("endGmtCreate")!= null ? LocalDateTime.parse(param.get("endGmtCreate").toString() , dateTimeFormatter) : null;
        //拼接动态条件语句的 sql 语句
        StringBuffer comditionalSQL = new StringBuffer();
        //排序语句的 sql 语句
        String orderbySQL = "order by t.gmtCreate asc";
        //主 sql 语句
        String baseSQL = "select t.id , t.scheduleId , t.ftpUrl , t.schemeName , t.parentId , t.gmtCreate , t.gmtModified , t.createBy , t.updateBy , t.isDeleted  , t2.status from MonitorSchemeTimerTaskEntity t inner join SysScheduleTaskEntity t2 on t.scheduleId = t2.id where t.isDeleted = 0 and t.schemeName like :schemeName ";
        //查询条数的 sql 语句
        String countSQL = "select count(*) from MonitorSchemeTimerTaskEntity t inner join SysScheduleTaskEntity t2 on t.scheduleId = t2.id where t.isDeleted = 0 and t.schemeName like :schemeName ";
        //map用来组装SQL占位符和对应的值
        Map<String, Object> map = new HashMap<String, Object>();
        //添加值
        map.put("schemeName" , "%"+schemeName+"%");
        //组装查询一级菜单或者二级菜单的情况
        if (StringUtils.isNotEmpty(parentId)) {
            comditionalSQL.append("and t.parentId <> '1' ");
        }else {
            comditionalSQL.append("and t.parentId = '1' ");
        }
        //组装查询日期
        if(startGmtCreate != null && endGmtCreate != null){
            comditionalSQL.append("and t.gmtCreate between :startGmtCreate and :endGmtCreate ");
            map.put("startGmtCreate" , startGmtCreate);
            map.put("endGmtCreate" , endGmtCreate);
        }
        //组装SQL
        String resSQL = baseSQL + comditionalSQL.toString() + orderbySQL;
        countSQL = countSQL + comditionalSQL;
        //创建查询对象
        Query res = this.entityManager.createQuery(resSQL);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            res.setParameter(entry.getKey(), entry.getValue());
        }
        Pageable pageable = org.springframework.data.domain.PageRequest.of(params.getPage() - 1, params.getSize());
        //设置分页对象
        res.setFirstResult((int) pageable.getOffset());
        res.setMaxResults(pageable.getPageSize());
        //创建 HQL 执行器
        Query countQuery = entityManager.createQuery(countSQL);

        //为执行器参数赋值
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        //返回条数
        Long totalCount = (Long) countQuery.getSingleResult();

        //返回数据
        List<Object[]> resultList = res.getResultList();

        //封装数据对象
        List<MonitorSchemeTimerTaskEntityDto> monitorSchemeTimerTasks = null;
        if(resultList != null){
            //创建集合
            monitorSchemeTimerTasks = new ArrayList<>();
            for(int i = 0 ; i < resultList.size() ; i++){
                //将对象取出
                Object[] obj = resultList.get(i);
                //创建对象
                MonitorSchemeTimerTaskEntityDto monitorSchemeTimerTask = new MonitorSchemeTimerTaskEntityDto();
                //进行强转赋值
                monitorSchemeTimerTask.setId(obj[0]+"");
                monitorSchemeTimerTask.setScheduleId(obj[1]+"");
                monitorSchemeTimerTask.setFtpUrl(obj[2]+"");
                monitorSchemeTimerTask.setSchemeName(obj[3]+"");
                monitorSchemeTimerTask.setParentId(obj[4]+"");
                monitorSchemeTimerTask.setGmtCreate((java.time.LocalDateTime)obj[5]);
                monitorSchemeTimerTask.setGmtModified((java.time.LocalDateTime)obj[6]);
                monitorSchemeTimerTask.setCreateBy(obj[7]+"");
                monitorSchemeTimerTask.setUpdateBy(obj[8]+"");
                monitorSchemeTimerTask.setIsDeleted((Long)obj[9]);
                monitorSchemeTimerTask.setStatus((int)obj[10]);
                monitorSchemeTimerTasks.add(monitorSchemeTimerTask);
            }
        }
        //转换成为 Page 对象进行值的
        Page<MonitorSchemeTimerTaskEntityDto> page = new PageImpl<>(monitorSchemeTimerTasks, pageable, totalCount.longValue());
        return page;
    }

    /**
     * 根据传入的 Json 数据进行构建一个父对象
     * @param jsonresult json 格式的数据
     */
    @Override
    public MonitorSchemeTimerTaskEntity addMonitorSchemeTimerTask(String jsonresult){
        MonitorSchemeTimerTaskEntity monitorSchemeTimerTaskEntity = null;
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonresult);
            String schemeName = jsonObject.get("schemeName")+"";
            MonitorSchemeTimerTaskEntity schemeTimerTaskEntity = new MonitorSchemeTimerTaskEntity();
            schemeTimerTaskEntity.setScheduleId("null");
            schemeTimerTaskEntity.setFtpUrl("");
            schemeTimerTaskEntity.setSchemeName(schemeName);
            schemeTimerTaskEntity.setGmtCreate(LocalDateTime.now());
            schemeTimerTaskEntity.setIsDeleted(ConstUtil.IS_NOT_DELETED);
            schemeTimerTaskEntity.setParentId("1");
            monitorSchemeTimerTaskEntity = inspectionRepo.save(schemeTimerTaskEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return monitorSchemeTimerTaskEntity;
    }

    /**
     * 根据传入的 MonitorSchemeTimerTaskEntity 对象数据进行更新/保存的操作
     * @param monitorSchemeTimerTask 对象数据
     * @return 保存的 MonitorSchemeTimerTaskEntity 数据对象
     */
    @Override
    public MonitorSchemeTimerTaskEntity addMonitorSchemeTimerTask(MonitorSchemeTimerTaskEntity monitorSchemeTimerTask){
        MonitorSchemeTimerTaskEntity monitorSchemeTimerTaskEntity = null;
        try {
            //设置共有值
            monitorSchemeTimerTask.setFtpUrl("");
            monitorSchemeTimerTask.setGmtCreate(LocalDateTime.now());
            monitorSchemeTimerTask.setIsDeleted(ConstUtil.IS_NOT_DELETED);
            monitorSchemeTimerTask.setParentId("1");
            //进行数据的保存操作
            monitorSchemeTimerTaskEntity = inspectionRepo.save(monitorSchemeTimerTask);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return monitorSchemeTimerTaskEntity;
    }

    /**
     * 根据传入的 ID 删除数据 如果数据 id 跟其他的子数据关联也会进行删除操作
     * @param id id 主键
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteMonitorSchemeTimerTask(String id) {
        //修改数据的删除标识 级联修改
        inspectionRepo.updateIsDeleteById(ConstUtil.IS_DELETED , id, id);
    }
}
