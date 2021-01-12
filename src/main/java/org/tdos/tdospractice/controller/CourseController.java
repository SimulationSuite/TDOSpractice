package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.AddCourse;
import org.tdos.tdospractice.body.ModifyCourseStatus;
import org.tdos.tdospractice.body.PrepareCourse;
import org.tdos.tdospractice.service.CourseService;
import org.tdos.tdospractice.type.Course;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.utils.PageTool;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/get_admin_course_list")
    public Response<Page<Course>> getAdminCourseList(@RequestParam(value = "per_page") Integer perPage,
                                                     @RequestParam(value = "page") Integer page) {
        return Response.success(PageTool.getPageList(courseService.getAdminCourseList(), page, perPage));
    }

    @GetMapping(value = "/get_admin_course_list_by_class_id")
    public Response<Page<Course>> getAdminCourseList(@RequestParam(value = "class_id") String classId,
                                                     @RequestParam(value = "per_page") Integer perPage,
                                                     @RequestParam(value = "page") Integer page) {
        return Response.success(PageTool.getPageList(courseService.getAdminCourseListByClassId(classId), page, perPage));
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
    public Response<Page<Course>> getCourseListById(@RequestParam(value = "user_id") String userId,
                                                    @RequestParam(value = "per_page") Integer perPage,
                                                    @RequestParam(value = "page") Integer page) {
        return Response.success(PageTool.getPageList(courseService.getCourseListById(userId), page, perPage));
    }

    // 管理员内置课程
    @PostMapping(value = "/insert_course")
    public Response<Course> insertCourse(@RequestBody AddCourse addCourse) {
        return Response.success(courseService.AddAdminCourse(addCourse));
    }

    // 发布课程
    @PostMapping(value = "/modify_course_status")
    public Response<String> modifyCourseStatus(@RequestBody ModifyCourseStatus modifyCourseStatus) {
        Pair<Boolean, String> pair = courseService.modifyCourseStatus(modifyCourseStatus);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    // 获取管理员未发布的课程
    @GetMapping(value = "/get_admin_unpublished_course_list")
    public Response<Page<Course>> getAdminUnpublishedCourseList(@RequestParam(value = "user_id") String userId,
                                                                @RequestParam(value = "per_page") Integer perPage,
                                                                @RequestParam(value = "page") Integer page) {
        return Response.success(PageTool.getPageList(courseService.getAdminUnpublishedCourseList(userId), page, perPage));
    }

    // 学生端查询课程
    @GetMapping(value = "/get_course_list")
    public Response<Page<Course>> getCourseList(@RequestParam(value = "user_id") String userId,
                                                @RequestParam(value = "start", required = false) String start,
                                                @RequestParam(value = "end", required = false) String end,
                                                @RequestParam(value = "per_page") Integer perPage,
                                                @RequestParam(value = "page") Integer page){
        return Response.success(PageTool.getPageList(courseService.getCourseList(userId, start, end), page, perPage));
    }


}
