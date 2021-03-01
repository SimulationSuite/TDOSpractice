package org.tdos.tdospractice.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.entity.ImageEntity;
import org.tdos.tdospractice.service.ImageService;
import org.tdos.tdospractice.type.Response;

import java.util.List;
import java.util.Map;

@RestController
public class ImageManageController {

    @Autowired
    private ImageService imageService;

    @GetMapping(value = "/getImageList")
    public Response<PageInfo<ImageEntity>> getImageList(@RequestParam(value = "kind") int kind, @RequestParam(value = "imageName") String imageName,
                                                        @RequestParam(value = "page") int page, @RequestParam(value = "perPage") int perPage) {
        return Response.success(imageService.getImageList(kind, imageName, page, perPage));
    }

    @GetMapping(value = "/getImagequoteList")
    public Response<PageInfo<Map<String, Object>>> getImagequoteList(@RequestParam(value = "kind") int kind, @RequestParam(value = "imageName") String imageName,
                                                                     @RequestParam(value = "page") int page, @RequestParam(value = "perPage") int perPage) {
        return Response.success(imageService.getImagequoteList(kind, imageName, page, perPage));
    }

    @GetMapping(value = "/addImage")
    public Response<String> addImage(@RequestParam(value = "imageName") String imageName, @RequestParam(value = "introduction") String introduction,
                                     @RequestParam(value = "kind") int kind, @RequestParam(value = "url") String url) {
        if (imageService.addImage(imageName, introduction, kind, url) < 0) {
            return Response.error();//image is exit
        }
        return Response.success();
    }

    @PostMapping(value = "/deleteImages")
    public Response<String> deleteImages(@RequestParam(value = "imagesID") List<String> imagesID) {
        if (imagesID.isEmpty() || imagesID.size() == 0) {
            return Response.error("系统异常，请稍后再试");
        }
        String result = imageService.deleteImages(imagesID);
        if (result != null) {
            return Response.error(result);
        }
        return Response.success();
    }
}
