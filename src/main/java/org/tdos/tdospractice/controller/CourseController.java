package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.AddCourse;
import org.tdos.tdospractice.body.ModifyCourseStatus;
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

    @GetMapping(value = "/get_course_list_by_user_id")
    public Response<List<Course>> getCourseListById(@RequestParam(value = "user_id") String userId) {
        return Response.success(courseService.getCourseListById(userId));
    }

    // 管理员内置课程
    @PostMapping(value = "/insert_course")
    public Response<Course> insertCourse(@RequestBody AddCourse addCourse) {
        return Response.success(courseService.AddAdminCourse(addCourse));
    }

    @PostMapping(value = "/modify_course_status")
    public Response<String> modifyCourseStatus(@RequestBody ModifyCourseStatus modifyCourseStatus) {
        Pair<Boolean, String> pair =  courseService.modifyCourseStatus(modifyCourseStatus);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }
}
