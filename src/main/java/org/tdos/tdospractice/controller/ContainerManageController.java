package org.tdos.tdospractice.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.entity.ContainerEntity;
import org.tdos.tdospractice.service.ContainerService;
import org.tdos.tdospractice.type.Response;

import java.util.List;
import java.util.Map;

@RestController
public class ContainerManageController {

    @Autowired
    private ContainerService containerService;

    @GetMapping(value = "/getRunContainerList")
    public Response<PageInfo<Map<String, Object>>> getRunContainerList(@RequestParam(value = "type") int type, @RequestParam(value = "classId") String classId,
                                                                       @RequestParam(value = "page") int page, @RequestParam(value = "perPage") int perPage) {
        PageInfo<Map<String, Object>> map = containerService.getRunContainerList(type, classId, page, perPage);
        if (map == null) {
            return Response.error();
        }
        return Response.success(map);
    }

    @PostMapping(value = "/stopRunContainerList")
    public Response<String> stopRunContainerList() {
        if (containerService.stopRunContainerList()) {
            return Response.error();
        }
        return Response.success();
    }

    @PostMapping(value = "/execContainer")
    public Response<String> execContainer(@RequestParam(value = "containerId") List<String> containerIds, @RequestParam(value = "type") int type) {
        if (containerService.execContainer(containerIds, type)) {
            return Response.error();
        }
        return Response.success();
    }

    @PostMapping(value = "/downloadCode")
    public Response<byte[]> downloadCode(@RequestParam(value = "containerId") String containerId, @RequestParam(value = "fileName") String fileName) {
        return Response.success(containerService.downloadCode(containerId, fileName));
    }

    @GetMapping(value = "/getRunExperiment")
    public Response<PageInfo<Map<String, Object>>> getRunExperiment(@RequestParam(value = "page") int page, @RequestParam(value = "perPage") int perPage) {
        return Response.success(containerService.getRunExperiment(page, perPage));
    }

    @GetMapping(value = "/getRunContainerByTeacher")
    public Response<PageInfo<Map<String, Object>>> getRunContainerByTeacher(@RequestParam(value = "courseId") String courseId, @RequestParam(value = "filter") String filter,
                                                                            @RequestParam(value = "page") int page, @RequestParam(value = "perPage") int perPage) {
        if (courseId == null || courseId.equals("")) {
            return Response.error();
        }
        return Response.success(containerService.getRunContainerByTeacher(courseId, filter, page, perPage));
    }

    @GetMapping(value = "/createContainers")
    public Response<List<ContainerEntity>> createContainers(@RequestParam(value = "userId") String userId, @RequestParam(value = "containerId") String experimentId) {
        return Response.success(containerService.createContainers(userId, experimentId));
    }
}
