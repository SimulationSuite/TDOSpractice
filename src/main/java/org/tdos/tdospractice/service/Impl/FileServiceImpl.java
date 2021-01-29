package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tdos.tdospractice.body.UploadFile;
import org.tdos.tdospractice.service.FileService;
import org.tdos.tdospractice.utils.FTPUtils;

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
    public Pair<Boolean, String> upload(MultipartFile file, int type) {
        if (!file.isEmpty()) {
            String fileName=file.getOriginalFilename();
            String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
            String uploadFileName= UUID.randomUUID().toString()+"-"+fileName;
            String path = System.getProperty("user.dir");
            File fileDir=new File(path);
            if (!fileDir.exists()){
                fileDir.setWritable(true);
                fileDir.mkdirs();
            }
            File targetFile=new File(path,uploadFileName);
            try {
                file.transferTo(targetFile);
                //file upload successful

                // upload targetFile to my FTP server
                Pair<Boolean, String> pair  = FTPUtils.uploadFile(targetFile,type);
                //  delete the file in upload folder after uploaded
                targetFile.delete();
                return pair;
            } catch (IOException e) {
//                log.error("文件上传异常",e);
                return new Pair<>(false,"");
            }
        } else  {
            return new Pair<>(false,"");
        }
    }

    @Override
    public boolean download() {
        return false;
    }
}
