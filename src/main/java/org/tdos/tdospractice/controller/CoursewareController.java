package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.CoursewareService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.CoursewareIdList;
import org.tdos.tdospractice.body.Courseware;
import java.util.*;

@RestController
public class CoursewareController {

    @Autowired
    private CoursewareService coursewareService;

    @GetMapping(value = "/getCoursewareByClassId")
    public Response<PageInfo<CoursewareEntity>> getCoursewareByClassId(@RequestParam(value = "classId") String classId,
                                                                       @RequestParam(value = "perPage") Integer perPage,
                                                                       @RequestParam(value = "page") Integer page) {
        return Response.success(coursewareService.getCoursewareByClassId(classId, perPage, page));
    }

    @GetMapping(value = "/getCoursewareBySectionId")
    public Response<PageInfo<CoursewareEntity>> getCoursewareBySectionId(@RequestParam(value = "sectionId") String sectionId,
                                                                         @RequestParam(value = "perPage") Integer perPage,
                                                                         @RequestParam(value = "page") Integer page) {
        return Response.success(coursewareService.getCoursewareBySectionId(sectionId, perPage, page));
    }

    @GetMapping(value = "/getCoursewareByChapterId")
    public Response<PageInfo<CoursewareEntity>> getCoursewareByChapterId(@RequestParam(value = "chapterId") String chapterId,
                                                                         @RequestParam(value = "perPage") Integer perPage,
                                                                         @RequestParam(value = "page") Integer page) {
        return Response.success(coursewareService.getCoursewareByChapterId(chapterId, perPage, page));
    }

    @GetMapping(value = "/getCoursewareByCourseId")
    public Response<PageInfo<CoursewareEntity>> getCoursewareByCourseId(@RequestParam(value = "courseId") String courseId,
                                                                        @RequestParam(value = "perPage") Integer perPage,
                                                                        @RequestParam(value = "page") Integer page) {
        return Response.success(coursewareService.getCoursewareByCourseId(courseId, perPage, page));
    }

    @PostMapping(value = "/deleteCoursewareById")
    public Response<Map<String, Object>> deleteCoursewareById(@RequestBody CoursewareIdList idList) {
        Map<String, Object> map = coursewareService.deleteCoursewareById(idList.coursewareIdList);
        return Response.success(map);
    }

    @PostMapping(value = "/addCourseware")
    public Response<CoursewareEntity> addCourseware(@RequestBody Courseware courseware) {
        return Response.success(coursewareService.addCourseware(courseware));
    }

    @PostMapping(value = "/modifyCoursewareNameById")
    public Response<Boolean> modifyCoursewareNameById(@RequestBody Courseware courseware) {
        return Response.success(coursewareService.modifyCoursewareNameById(courseware));
    }

}

