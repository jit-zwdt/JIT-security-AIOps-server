package com.jit.controller;

import com.jit.Application;
import com.jit.server.pojo.SysCronExpressionEntity;
import com.jit.server.service.AssetsService;
import com.jit.server.service.SysCronExpressionService;
import com.jit.server.util.FtpClientUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

/**
 * 测试类
 * 测试代码 暂时只放了 测试 FTP 下载代码的测试
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

    /**
     * 测试 FTP 文件下载
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
    public void testAddCronExpression(){
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
    public void testGetCronExpressionDesc(){
        boolean flag = sysCronExpressionService.checkAddCronExpressionDesc("每隔10");
        System.out.println(flag);
    }

    /**
     * 测试删除方法
     */
    @Test
    public void testDelCronExpression(){
        sysCronExpressionService.delCronExpression("1");
    }
}
