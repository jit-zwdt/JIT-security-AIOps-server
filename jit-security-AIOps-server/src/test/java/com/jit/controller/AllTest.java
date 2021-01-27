package com.jit.controller;

import com.jit.Application;
import com.jit.server.dto.SysLogDTO;
import com.jit.server.pojo.SysCronExpressionEntity;
import com.jit.server.service.AssetsService;
import com.jit.server.service.SysCronExpressionService;
import com.jit.server.service.SysLogService;
import com.jit.server.util.FtpClientUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 测试类
 * 测试代码 暂时只放了 测试 FTP 下载代码的测试
 *
 * @author oldwang <br />
 * 创建时间: 2020.10.22
 */
@SpringBootTest(classes = {Application.class})
@RunWith(SpringRunner.class)
public class AllTest {

    @Autowired
    private AssetsService assetsService;

    @Autowired
    private SysCronExpressionService sysCronExpressionService;

    @Autowired
    private SysLogService sysLogService;

    /**
     * 测试 FTP 文件下载
     *
     * @throws IOException
     */
    @Test
    public void testFtpPdfDownload() throws IOException {
        //创建 FtpUtil 对象
        FtpClientUtil ftpClientUtil = new FtpClientUtil();
        //获取 FTP 连接对象
        FTPClient ftpClient = ftpClientUtil.getConnectionFTP("172.16.15.10", 21, "jitutil", "dota&csjit3368");
        //输出流构建
        OutputStream os = new FileOutputStream(new File("D://a.pdf"));
        //获取文件并打入流中
        ftpClient.retrieveFile("/20201022/oldwang 测试巡检数据01/2020102215/631255a6-778b-46ef-884f-ca43f65b3106.pdf", os);
        //关闭流对象
        os.close();
    }

    /**
     * 测试添加时间表达式对象
     */
    @Test
    public void testAddCronExpression() {
        //创建需要添加的对象
        SysCronExpressionEntity cronExpression = new SysCronExpressionEntity();
        cronExpression.setCronExpressionDesc("1111");
        cronExpression.setCronExpression("* * * * * *");
        //测试添加
        SysCronExpressionEntity cronExpressionEntity = sysCronExpressionService.addCronExpression(cronExpression);
        System.out.println(cronExpressionEntity);
    }

    /**
     * 测试是有有对应的数据
     */
    @Test
    public void testGetCronExpressionDesc() {
        boolean flag = sysCronExpressionService.checkAddCronExpressionDesc("每隔10");
        System.out.println(flag);
    }

    /**
     * 测试删除方法
     */
    @Test
    public void testDelCronExpression() {
        sysCronExpressionService.delCronExpression("1");
    }

    /**
     * 测试 导出 Xls 文件书写方法
     *
     * @throws IOException
     */
    @Test
    public void createXls() throws IOException {
        //需要生成的表格数据
        String[][] dataArray = {{"出现问题", "0", "", "16"}, {"认领问题", "0", "", "1"}, {"处理中问题", "0", "", "0"}, {"解决问题", "0", "", "1"}};
        //添加的数据的条数
        int rowSize = dataArray.length;
        //存储的文件名称
        String fileName = "D:/运维日报.xls";
        //大标题的名称
        String headName = "运维日报";
        //安全的时间转换类对象的声明
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        //转换时间字符串
        String dataStr = formatter.format(LocalDateTime.now());
        //表头
        String[] tableHeader = {"类别", "当日新增数", "当日新增详情", "本月总数"};
        //运维人
        String roleName = "管理员";
        //存储的文件
        File file = new File(fileName);
        //表的列数
        short cellNumber = (short) tableHeader.length;
        //创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个工作表
        HSSFSheet sheet = workbook.createSheet(headName);
        //设置列宽
        sheet.setColumnWidth(0, 256 * 15 + 184);
        sheet.setColumnWidth(1, 256 * 10 + 184);
        sheet.setColumnWidth(2, 256 * 50 + 184);
        sheet.setColumnWidth(3, 256 * 20 + 184);
        //创建合并的单元格
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, cellNumber - 1);
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
        font.setFontHeightInPoints((short) 30);
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
        headCell.setCellValue("运维人:" + roleName);
        headCell.setCellStyle(cellTextStyle);

        //创建时间
        headCell = hssfRow.createCell(cellNumber - 1);
        headCell.setCellValue("时间:" + dataStr);
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
        for (int i = 0; i < cellNumber; i++) {
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
        for (int i = 0; i < rowSize; i++) {
            hssfRow = sheet.createRow(2 + 1 + i);
            //添加文字
            for (int b = 0; b < dataArray[i].length; b++) {
                // 添加内容
                headCell = hssfRow.createCell(b);
                headCell.setCellValue(dataArray[i][b]);
                headCell.setCellStyle(cellTextStyle);
            }
        }
        //添加尾行
        hssfRow = sheet.createRow(2 + 1 + rowSize);
        //设置行高
        hssfRow.setHeight((short) 500);
        //创建正常的文字样式
        cellTextStyle = workbook.createCellStyle();
        //设置文字居中
        cellTextStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置上下的边框
        cellTextStyle.setBorderTop(BorderStyle.THIN);
        cellTextStyle.setBorderBottom(BorderStyle.THIN);
        //设置整个行的上下边框
        for (int i = 0; i < cellNumber - 1; i++) {
            headCell = hssfRow.createCell(i);
            headCell.setCellStyle(cellTextStyle);
        }
        //创建正常的文字样式
        cellTextStyle = workbook.createCellStyle();
        //设置文字居中
        cellTextStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellTextStyle.setBorderTop(BorderStyle.THIN);
        cellTextStyle.setBorderBottom(BorderStyle.THIN);
        cellTextStyle.setBorderLeft(BorderStyle.THIN);
        //设置文字
        headCell = hssfRow.createCell(0);
        headCell.setCellValue("负责人(签字)");
        headCell.setCellStyle(cellTextStyle);
        //创建正常的文字样式
        cellTextStyle = workbook.createCellStyle();
        //设置文字居中
        cellTextStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellTextStyle.setBorderTop(BorderStyle.THIN);
        cellTextStyle.setBorderBottom(BorderStyle.THIN);
        cellTextStyle.setBorderRight(BorderStyle.THIN);
        //设置文字
        headCell = hssfRow.createCell(cellNumber - 1);
        headCell.setCellValue("日期");
        headCell.setCellStyle(cellTextStyle);
        //写入到文件
        workbook.write(file);
    }

    @Test
    public void testFindSysLog() {
        LocalDateTime startTime = LocalDateTime.parse("2020-12-23T00:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2020-12-24T00:00:00");
        Page<SysLogDTO> sysLog = sysLogService.findSysLog(1, "定时", startTime, endTime, 2, 1, 5);
        System.out.println(sysLog.getContent());
        System.out.println(sysLog.getTotalElements());
        System.out.println(sysLog.getSize());
    }
}
