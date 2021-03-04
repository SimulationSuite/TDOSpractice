package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tdos.tdospractice.body.UploadFile;
import org.tdos.tdospractice.service.FileService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.utils.FTPUtils;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${ftp.ftpIp}")
    private String ftpIp;

    @Value("${ftp.ftpUser}")
    private String ftpUser;

    @Value("${ftp.ftpPass}")
    private String ftpPass;

    @Override
    public Pair<Boolean, UploadFile> upload(MultipartFile file, int type) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String uppath = "";
            String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
            String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
            String path = System.getProperty("user.dir");
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.setWritable(true);
                fileDir.mkdirs();
            }
            File targetFile = new File(path, uploadFileName);
            try {
                file.transferTo(targetFile);
                int pages = 0;
                Long time = 0L;
                if (type == 0) {//图片
                    uppath = "/pic/";
                } else if (type == 1) {//视频findSelectedExperimentByCategory
                    uppath = "/video/";
                    time = getVideoTime(targetFile);
                } else if (type == 2) {//PDF
                    uppath = "/courseware/";
                    PDDocument pdDocument = PDDocument.load(targetFile);
                    pages = pdDocument.getNumberOfPages();
                }
                //file upload successful

                // upload targetFile to my FTP server
                if (FTPUtils.uploadFile(targetFile, type,ftpIp,ftpUser,ftpPass).getKey()) {
                    Pair<Boolean, UploadFile> pair = new Pair<>(true, UploadFile
                            .builder()
                            .size(file.getSize())
                            .name(uppath + FTPUtils.uploadFile(targetFile, type,ftpIp,ftpUser,ftpPass).getValue())
                            .pages(pages)
                            .time(time)
                            .build());
                    targetFile.delete();
                    return pair;
                }

                //  delete the file in upload folder after uploaded
                targetFile.delete();
                return new Pair<>(false, new UploadFile());
            } catch (IOException e) {
                return new Pair<>(false, new UploadFile());
            }
        } else {
            return new Pair<>(false, new UploadFile());
        }
    }

    @Override
    public boolean download() {
        return false;
    }

    @Override
    public boolean delete(String path) {
        try {
            return FTPUtils.deleteFile("/data/"+path.split("/")[1],path.split("/")[2],ftpIp,ftpUser,ftpPass);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Long getVideoTime(File file) {
        try {
            MultimediaObject instance = new MultimediaObject(file);
            MultimediaInfo result = instance.getInfo();
            long ls = result.getDuration() / 1000;
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }
}
