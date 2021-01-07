package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.PrepareCourse;
import org.tdos.tdospractice.service.CourseService;
import org.tdos.tdospractice.type.Course;
import org.tdos.tdospractice.type.Response;

import java.util.List;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/get_admin_course_list")
    public Response<List<Course>> getAdminCourseList() {
        return Response.success(courseService.getAdminCourseList());
    }

    @GetMapping(value = "/get_admin_course_list_by_class_id")
    public Response<List<Course>> getAdminCourseList(@RequestParam(value = "class_id") String classId) {
        return Response.success(courseService.getAdminCourseListByClassId(classId));
    }

    @PostMapping(value = "/prepare_course")
    public Response<String> prepareCourse(@RequestBody PrepareCourse prepareCourse) {
        Pair<Boolean, String> pair = courseService.prepareCourse(prepareCourse);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

}
