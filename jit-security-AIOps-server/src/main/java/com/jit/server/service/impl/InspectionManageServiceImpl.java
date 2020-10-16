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
import com.jit.server.repository.InspectionRepo;
import com.jit.server.service.InspectionManageService;
import com.jit.server.util.FtpClientUtil;
import com.jit.server.util.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;
import java.util.UUID;

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
            makepdf(filepath);
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

    public String makepdf(String filepath) throws Exception {
        if (filepath == null) {
            throw new Exception("pdf路径为空");
        }
        String url = "";
        File file = new File(filepath);
        if (file.exists()) {
            FileInputStream input = null;
            try {
                input = new FileInputStream(file);
                url = makeftp(input, filepath);
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

    public String makeftp(FileInputStream file, String filepath) throws Exception {
        FTPClient ftp = null;
        String url = "";
        try {
            if (file != null) {
                String path = "/patrol/";
                FtpClientUtil a = new FtpClientUtil();
                ftp = a.getConnectionFTP(ftpConfig.getHostName(), ftpConfig.getPort(), ftpConfig.getUserName(), ftpConfig.getPassWord());
                url = a.uploadFile(ftp, path, filepath, file);
                url = url.replace("/", "");
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

}
