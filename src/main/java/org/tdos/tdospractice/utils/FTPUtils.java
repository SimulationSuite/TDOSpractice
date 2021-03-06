package org.tdos.tdospractice.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javafx.util.Pair;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;

@Data
@Slf4j(topic = "ftp")
public class FTPUtils {
    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtils(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    private boolean connectServer(String ip, String user, String pwd) {
        ftpClient = new FTPClient();
        Boolean isSuccess = false;
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            log.error("连接ftp服务器失败", e);
        }
        return isSuccess;
    }

    private Pair<Boolean, String> uploadFile(int type, File file, String ftpIp, String ftpUser, String ftpPass) throws IOException {
        Boolean upload = false;
        FileInputStream fileInputStream = null;
        //connect to ftpServer
        if (connectServer(ftpIp, ftpUser, ftpPass)) {
            try {
                String remotePath = null;
                switch (type) {
                    case 0:
                        remotePath = "/data/pic";
                        break;
                    case 1:
                        remotePath = "/data/video";
                        break;
                    case 2:
                        remotePath = "/data/courseware";
                        break;
                }
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                fileInputStream = new FileInputStream(file);
                ftpClient.storeFile(file.getName(), fileInputStream);
                upload = true;
            } catch (IOException e) {
                log.error("上传文件异常", e);
                upload = false;
            } finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }
        }
        return new Pair<>(upload, file.getName());
    }

    public boolean downLoadFile(String filePath, String fileName, String downPath) {
        boolean download = true;
        try {
            if (connectServer(this.ip, this.user, this.pwd)) {
                try {
                    ftpClient.changeWorkingDirectory(filePath);
                    ftpClient.setBufferSize(1024);
                    ftpClient.setControlEncoding("UTF-8");
                    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                    ftpClient.enterLocalPassiveMode();
                    FTPFile[] files = ftpClient.listFiles();
                    for (FTPFile file : files) {
                        // 取得指定文件并下载
                        if (file.getName().equals(fileName)) {
                            File downFile = new File(downPath + File.separator
                                    + file.getName());
                            OutputStream out = new FileOutputStream(downFile);
                            // 绑定输出流下载文件,需要设置编码集，不然可能出现文件为空的情况
                            download = ftpClient.retrieveFile(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"), out);
                            // 下载成功删除文件,看项目需求
                            // ftp.deleteFile(new String(fileName.getBytes("UTF-8"),"ISO-8859-1"));
                            out.flush();
                            out.close();
                            if (download) {
                                System.out.println("上传成功");
                            } else {
                                System.out.println("上传失败");
                            }
                        }
                    }
                } catch (IOException e) {
                    log.error("上传文件异常", e);
                } finally {
                    ftpClient.disconnect();
                }
            }
        } catch (Exception e) {
            System.out.println("下载失败");
        }

        return download;
    }

    public boolean delete(String filePath, String filename, String ftpIp, String ftpUser, String ftpPass) {
        boolean delete = false;
        try {
            if (connectServer(ftpIp, ftpUser, ftpPass)) {
                try {
                    ftpClient.changeWorkingDirectory(filePath);
                    FTPFile[] files = ftpClient.listFiles();
                    for (FTPFile file : files) {
                        if (file.getName().equals(filename)) {
                            ftpClient.dele(filename);
                            delete = true;
                        }
                    }
                } catch (IOException e) {
                    log.error("删除异常", e);
                } finally {
                    ftpClient.disconnect();
                }
            }
        } catch (Exception e) {
            System.out.println("删除失败");
        }

        return delete;
    }


    public static Pair<Boolean, String> uploadFile(File file, int type, String ftpIp, String ftpUser, String ftpPass) throws IOException {
        FTPUtils ftpUtil = new FTPUtils(ftpIp, 21, ftpUser, ftpPass);
        log.info("开始连接ftp服务器");
        log.info("开始连接ftp服务器，结束上传，上传结果{}");
        return ftpUtil.uploadFile(type, file, ftpIp, ftpUser, ftpPass);
    }

    public static boolean deleteFile(String filePath, String filename, String ftpIp, String ftpUser, String ftpPass) throws IOException {
        FTPUtils ftpUtil = new FTPUtils(ftpIp, 21, ftpUser, ftpPass);
        log.info("开始连接ftp服务器");
        return ftpUtil.delete(filePath, filename, ftpIp, ftpUser, ftpPass);
    }


//    public static void main(String[] args) {
//
//        FTPUtils ftpUtil = new FTPUtils("192.168.1.228", 21, "test", "123456");
//        if (ftpUtil.delete("data/", "test.png")) {
//            System.out.println("true");
//        } else {
//            System.out.println("false");
//        }
//    }
}
