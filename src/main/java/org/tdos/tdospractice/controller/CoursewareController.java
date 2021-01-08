package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.config.TDOSSessionListener;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.service.SecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.CoursewareService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.CoursewareIdList;
import org.tdos.tdospractice.body.Courseware;
import org.tdos.tdospractice.utils.OnlineStudent;

import static org.tdos.tdospractice.utils.Constants.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@RestController
public class CoursewareController {

    @Autowired
    private CoursewareService coursewareService;

    @Autowired
    private ClassService classService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = "/getCoursewareByClassId")
    public Response<List<CoursewareEntity>> getCoursewareByClassId(@RequestParam(value = "classId") String classId) {
        return Response.success(coursewareService.getCoursewareByClassId(classId));
    }

    @GetMapping(value = "/getCoursewareBySectionId")
    public Response<List<CoursewareEntity>> getCoursewareBySectionId(@RequestParam(value = "sectionId") String sectionId) {
        return Response.success(coursewareService.getCoursewareBySectionId(sectionId));
    }

    @GetMapping(value = "/getCoursewareByChapterId")
    public Response<List<CoursewareEntity>> getCoursewareByChapterId(@RequestParam(value = "chapterId") String chapterId) {
        return Response.success(coursewareService.getCoursewareByChapterId(chapterId));
    }

    @GetMapping(value = "/getCoursewareByCourseId")
    public Response<List<CoursewareEntity>> getCoursewareByCourseId(@RequestParam(value = "courseId") String courseId) {
        return Response.success(coursewareService.getCoursewareByCourseId(courseId));
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

