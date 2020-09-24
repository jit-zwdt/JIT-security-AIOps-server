package com.jit.server.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jit.server.pojo.HostEntity;
import com.jit.server.repository.HostRepo;
import com.jit.server.repository.InspectionRepo;
import com.jit.server.service.InspectionManageService;
import com.jit.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class InspectionManageServiceImpl implements InspectionManageService {
    @Autowired
    private InspectionRepo inspectionRepo;

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
    public String createPDF() throws Exception {
        try {
            String filename = UUID.randomUUID().toString();
            String basePath = getResourceBasePath();
            String resourcePath = new File(basePath, "src/main/java/com/jit/server/file/pdf/" + filename + ".pdf").getAbsolutePath();
            ensureDirectory(resourcePath);
            String filepath = resourcePath;
            makeDocumentdata(filepath);
            return filepath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void makeDocumentdata(String filepath) {
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
            PdfPTable table = createHeaderTable();
            document.add(table);
            Paragraph blankRow = new Paragraph(18f, " ", title40);
            document.add(blankRow);
            PdfPTable tableChild = createChildTable();
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

    public static PdfPTable createHeaderTable() throws DocumentException, IOException {
        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 12,Font.NORMAL);

        PdfPTable table = new PdfPTable(4); // 3 columns.
        table.setWidthPercentage(100); // Width 100%
        table.setSpacingBefore(10f); // Space before table
        table.setSpacingAfter(10f); // Space after table
        table.setTotalWidth(500); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        PdfPCell cell;

        // Set Column widths
//        float[] columnWidths = { 1f, 1f, 1f, 1f};
//        table.setWidths(columnWidths);
//        int size = 15;

        cell = new PdfPCell(new Paragraph("巡检对象", font));
//        cell1.setBorderColor(BaseColor.BLUE);
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("服务器【Windows0717】", font));
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

        return table;
    }

    public static PdfPTable createChildTable() throws DocumentException, IOException {
        BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 12,Font.NORMAL);
        Font font8 = new Font(bfChinese, 8,Font.NORMAL);

        PdfPTable table = new PdfPTable(4); // 3 columns.
        table.setWidthPercentage(100); // Width 100%
        table.setSpacingBefore(10f); // Space before table
        table.setSpacingAfter(10f); // Space after table
        table.setTotalWidth(500); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        PdfPCell cell;

        cell = new PdfPCell(new Paragraph("检查项目", font));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("结果", font));
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
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("内存", font8));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("正常", font8));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("内存使用率：20%", font8));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("磁盘空间", font8));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("异常", font8));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("磁盘空间使用率：98%！请联系负责人进行排查。", font8));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(17);
        cell.setColspan(2);
        table.addCell(cell);

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
            // nothing to do
        }
        if (path == null || !path.exists()) {
            path = new File("");
        }

        String pathStr = path.getAbsolutePath();
        // 如果是在eclipse中运行，则和target同级目录,如果是jar部署到服务器，则默认和jar包同级
        pathStr = pathStr.replace("\\target\\classes", "");

        return pathStr;
    }
}
