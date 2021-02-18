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

    private static final int PICSIZE = 10485760;
    private static final int VIDEOSIZE = 524288000;
    private static final int COURSEWARESIZE = 104857600;


    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Response upload(int type, @RequestParam(value = "file") MultipartFile multipartFile) {
        String uppath = "";
        if (type == 0) {//图片
            if (multipartFile.getSize() > PICSIZE)
                return Response.error("图片超过最大尺寸");
            uppath = "/pic/";
        } else if (type == 1) {//视频
            if (multipartFile.getSize() > VIDEOSIZE)
                return Response.error("视频超过最大尺寸");
            uppath = "/video/";
        } else if (type == 2) {//PDF
            if (multipartFile.getSize() > COURSEWARESIZE)
                return Response.error("课件超过最大尺寸");
            uppath = "/courseware/";
        }else {
            return Response.error("type error");
        }
        Pair<Boolean,UploadFile> pair = fileService.upload(multipartFile, type);
        if (pair.getKey()) {
            return Response.success(pair.getValue());
        }else {
            return Response.error("上传失败");
        }
    }

}
