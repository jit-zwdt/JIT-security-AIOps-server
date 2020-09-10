package com.jit.server.util;


import org.apache.commons.net.ftp.*;

import java.io.*;
import java.util.UUID;

public class FtpClientUtil {

    /**
     * 获得连接FTP方式
     *
     * @param hostName FTP服务器地址
     * @param port     FTP服务器端口
     * @param userName FTP登录用户名
     * @param passWord FTP登录密码
     * @return FTPClient
     */
    public FTPClient getConnectionFTP(String hostName, int port, String userName, String passWord) {
        //创建FTPClient对象
        FTPClient ftp = new FTPClient();
        try {
            //连接FTP服务器
            ftp.connect(hostName, port);
            ftp.setControlEncoding("UTF-8");
            ftp.setConnectTimeout(1000*30);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //登录ftp
            ftp.login(userName, passWord);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.disconnect();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return ftp;
    }

    /**
     * 关闭连接FTP方式
     *
     * @param ftp FTPClient对象
     * @return boolean
     */
    public boolean closeFTP(FTPClient ftp) {
        if (ftp !=null) {
            try {
                ftp.logout();
                ftp.disconnect();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 上传文件FTP方式
     *
     * @param ftp         FTPClient对象
     * @param path        FTP服务器上传地址
     * @param fileName    本地文件路径
     * @param inputStream 输入流
     * @return boolean
     */
    public String uploadFile(FTPClient ftp, String path, String fileName, InputStream inputStream) {
        boolean status = fileName.contains("/");
        if(status){
            String[] a = fileName.split("/");
            try {
                for (int i=0;i<a.length-1;i++) {
                    if (!ftp.changeWorkingDirectory(a[i])) {
                        ftp.makeDirectory(a[i]);
                        ftp.changeWorkingDirectory(a[i]);
                    }
                    path += a[i]+"/";
                }
                fileName = fileName.substring(fileName.lastIndexOf("/"),fileName.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //重新命名不重复的文件名
            fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."), fileName.length());
            path = new String(path.getBytes("GBK"), "ISO-8859-1");
            //转到指定上传目录
            ftp.changeWorkingDirectory(path);
            //将上传文件存储到指定目录
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //如果缺省该句 传输txt正常 但图片和其他格式的文件传输出现乱码
            ftp.storeFile(fileName, inputStream);
            //关闭输入流
            inputStream.close();
            //退出ftp
            ftp.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path + fileName;
    }

    /**
     * 删除文件FTP方式
     *
     * @param ftp      FTPClient对象
     * @param path     FTP服务器上传地址
     * @param fileName FTP服务器上要删除的文件名
     * @return
     */
    public boolean deleteFile(FTPClient ftp, String path, String fileName) {
        boolean success = false;
        try {
            ftp.changeWorkingDirectory(path);//转移到指定FTP服务器目录
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            path = new String(path.getBytes("GBK"), "ISO-8859-1");
            ftp.deleteFile(fileName);
            ftp.logout();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 下载文件FTP方式
     *
     * @param ftp       FTPClient对象
     * @param path      FTP服务器上传地址
     * @param fileName  本地文件路径
     * @param localPath 本里存储路径
     * @return boolean
     */
    public boolean downFile(FTPClient ftp, String path, String fileName, String localPath) {
        boolean success = false;
        try {
            ftp.changeWorkingDirectory(path);//转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles(); //得到目录的相应文件列表
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "\\" + ff.getName());
                    OutputStream outputStream = new FileOutputStream(localFile);
                    //将文件保存到输出流outputStream中
                    ftp.retrieveFile(new String(ff.getName().getBytes("GBK"), "ISO-8859-1"), outputStream);
                    outputStream.flush();
                    outputStream.close();
                    System.out.println("下载成功");
                }
            }
            ftp.logout();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 判断是否有重名文件
     *
     * @param fileName
     * @param fs
     * @return
     */
    public static boolean isFileExist(String fileName, FTPFile[] fs) {
        for (int i = 0; i < fs.length; i++) {
            FTPFile ff = fs[i];
            if (ff.getName().equals(fileName)) {
                // 如果存在返回 正确信号
                return true;
            }
        }
        // 如果不存在返回错误信号
        return false;
    }

    public byte[] readInputStream(InputStream inStream)
            throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        //inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}
