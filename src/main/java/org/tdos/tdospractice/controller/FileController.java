package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tdos.tdospractice.body.UploadFile;
import org.tdos.tdospractice.service.FileService;
import org.tdos.tdospractice.type.Response;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Response upload(int type, @RequestParam(value = "file") MultipartFile multipartFile) {
        Pair<Boolean,String> pair = fileService.upload(multipartFile, type);
        String uppath = "";
        if (type == 0) {//图片
            uppath = "/pic/";
        } else if (type == 1) {//视频
            uppath = "/video/";
        } else if (type == 2) {//PDF
            uppath = "/courseware/";
        }else {
            return Response.error("type error");
        }
        if (pair.getKey()) {
            UploadFile
                    .builder()
                    .size(multipartFile.getSize())
                    .name(uppath + pair.getValue()).build();
            System.out.println("1111");
            return Response.success();
        }else {
            return Response.error("上传失败");
        }
    }

}
