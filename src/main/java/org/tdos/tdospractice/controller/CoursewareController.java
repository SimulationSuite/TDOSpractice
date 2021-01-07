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

    @GetMapping(value = "/deleteCoursewareById")
    public Response<Integer> deleteCoursewareById(@RequestParam(value = "id")  String id) {
        return Response.success(coursewareService.deleteCoursewareById(id));
    }

    @GetMapping(value = "/addCourseware")
    public Response<CoursewareEntity> addCourseware(@RequestParam(value = "name")  String name, @RequestParam(value = "type")  int type, @RequestParam(value = "kind")  int kind, @RequestParam(value = "url")  String url) {
        return Response.success(coursewareService.addCourseware(name, type, kind, url));
    }

    @GetMapping(value = "/ifSectionCoursewareByCoursewareId")
    public Response<Integer> ifSectionCoursewareByCoursewareId(@RequestParam(value = "id")  String id) {
        return Response.success(coursewareService.ifSectionCoursewareByCoursewareId(id));
    }

    @GetMapping(value = "/modifyCoursewareNameById")
    public Response<Integer> modifyCoursewareNameById(@RequestParam(value = "id")  String id, @RequestParam(value = "name")  String name) {
        return Response.success(coursewareService.modifyCoursewareNameById(id, name));
    }

}

