package com.jit.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jit.server.config.FtpConfig;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
public class InspectionManageServiceImpl implements InspectionManageService {
    @Autowired
    private InspectionRepo inspectionRepo;
    @Autowired
    private FtpConfig ftpConfig;
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
                String schemeName =  jsonObject.get("schemeName")+"";
                if (schemeName == null || schemeName.equals("")) {
                    schemeName = "巡检计划";
                }
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHH");
                SimpleDateFormat sft = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date();
                String path = "/"+sft.format(date)+"/"+schemeName+"/"+sf.format(date)+"/";
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
    public Page<MonitorSchemeTimerTaskEntity> getMonitorSchemeTimerTasks(PageRequest<Map<String, Object>> params) {
        Map<String, Object> param = params.getParam();
        if (param != null && !param.isEmpty()) {
            //条件
            Specification<MonitorSchemeTimerTaskEntity> spec = new Specification<MonitorSchemeTimerTaskEntity>() {
                @Override
                public Predicate toPredicate(Root<MonitorSchemeTimerTaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> list = new ArrayList<Predicate>();
                    list.add(cb.equal(root.get("isDeleted").as(Integer.class), ConstUtil.IS_NOT_DELETED));
                    String jobClassName = param.get("schemeName") != null ? param.get("schemeName").toString() : "";
                    if (StringUtils.isNotBlank(jobClassName)) {
                        list.add(cb.like(root.get("schemeName").as(String.class), "%" + jobClassName + "%"));
                    }
                    // 如果 parentId 不是空的情况下则查询所有的 二级菜单
                    String parentId = param.get("parentId") + "";
                    if(parentId != null && !parentId.equals("")){
                        //查询所有的一级菜单的条件
                        list.add(cb.equal(root.get("parentId").as(String.class), "1"));
                    } else {
                        //查询所有的二级菜单的条件
                        list.add(cb.notEqual(root.get("parentId").as(String.class), "1"));
                    }

                    Predicate[] arr = new Predicate[list.size()];
                    return cb.and(list.toArray(arr));
                }
            };
            //排序的定义
            List<Sort.Order> orderList = new ArrayList<>();
            orderList.add(new Sort.Order(Sort.Direction.DESC, "gmtCreate"));
            Sort sort = Sort.by(orderList);
            //分页的定义
            Pageable pageable = org.springframework.data.domain.PageRequest.of(params.getPage() - 1, params.getSize(), sort);
            return inspectionRepo.findAll(spec, pageable);
        }
        return null;
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
}
