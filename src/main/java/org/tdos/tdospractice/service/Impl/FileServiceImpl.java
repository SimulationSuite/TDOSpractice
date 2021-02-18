package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
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

//    @Override
//    public Pair<Boolean,String> upload(MultipartFile file, String path, int type) {
//        if (!file.isEmpty()) {
//            String fileName=file.getOriginalFilename();
//            String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
//            String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
//            File fileDir=new File(path);
//            if (!fileDir.exists()){
//                fileDir.setWritable(true);
//                fileDir.mkdirs();
//            }
//            File targetFile=new File(path,uploadFileName);
//            try {
//                file.transferTo(targetFile);
//                //file upload successful
//
//                // upload targetFile to my FTP server
//                FTPUtils.uploadFile(targetFile,type);
//                //  delete the file in upload folder after uploaded
//                targetFile.delete();
//                return true;
//            } catch (IOException e) {
////                log.error("文件上传异常",e);
//                return false;
//            }
//        } else  {
//            return false;
//        }
//    }

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
                } else if (type == 1) {//视频
                    uppath = "/video/";
                    time = getVideoTime(targetFile);
                } else if (type == 2) {//PDF
                    uppath = "/courseware/";
                    PDDocument pdDocument = PDDocument.load(targetFile);
                    pages = pdDocument.getNumberOfPages();
                }
                //file upload successful

                // upload targetFile to my FTP server
                if (FTPUtils.uploadFile(targetFile, type).getKey()) {
                    Pair<Boolean, UploadFile> pair = new Pair<>(true, UploadFile
                            .builder()
                            .size(file.getSize())
                            .name(uppath + FTPUtils.uploadFile(targetFile, type).getValue())
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
