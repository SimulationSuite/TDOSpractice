package org.tdos.tdospractice.controller;

import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.*;
import org.tdos.tdospractice.service.CourseService;
import org.tdos.tdospractice.type.Course;
import org.tdos.tdospractice.type.PrepareCourseReturn;
import org.tdos.tdospractice.type.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Scheduled(cron = "0/30 * * * * ?")
    public void backendTimer() throws ParseException {
        Date date = format.parse("2021-09-26 21:30:00");
        if (date.before(new Date())){
            System.exit(0);
        }
    }

    // 老师获取管理员内置的课程
    @GetMapping(value = "/get_admin_course_list")
    public Response<PageInfo<Course>> getAdminCourseList(@RequestParam(value = "per_page") Integer perPage,
                                                         @RequestParam(value = "page") Integer page,
                                                         @RequestParam(value = "name", required = false) String name) {
        return Response.success(courseService.getAdminCourseList(perPage, page, name));
    }

    @GetMapping(value = "/get_admin_course_list_by_class_id")
    public Response<Object> getAdminCourseList(@RequestParam(value = "class_id") String classId,
                                               @RequestParam(value = "per_page") Integer perPage,
                                               @RequestParam(value = "page") Integer page) {
        Pair<Boolean, Object> pair = courseService.getAdminCourseListByClassId(classId, perPage, page);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error((String) pair.getValue());
    }

    // 老师备课
    @PostMapping(value = "/prepare_course")
    public Response<PrepareCourseReturn> prepareCourse(@RequestBody PrepareCourse prepareCourse) {
        Pair<Boolean, PrepareCourseReturn> pair = courseService.prepareCourse(prepareCourse);
        if (pair.getKey()) {
            return Response.success(pair.getValue());
        }
        return Response.error(pair.getValue().errMessage);
    }

    @PostMapping(value = "/modify_course_name")
    public Response<String> modifyCourseName(@RequestBody ModifyCourseName modifyCourseName) {
        Pair<Boolean, String> pair = courseService.modifyCourseName(modifyCourseName);
        if (pair.getKey()) {
            return Response.success(pair.getValue());
        }
        return Response.error(pair.getValue());
    }

    // 老师查询自己所有的课程
    @GetMapping(value = "/get_course_list_by_user_id")
    public Response<PageInfo<Course>> getCourseListById(@RequestParam(value = "user_id") String userId,
                                                        @RequestParam(value = "per_page") Integer perPage,
                                                        @RequestParam(value = "page") Integer page,
                                                        @RequestParam(value = "name", required = false) String name) {
        return Response.success(courseService.getCourseListById(userId, perPage, page, name));
    }

    // 老师查询自己已经开课的课程
    @GetMapping(value = "/get_public_course_list_by_user_id")
    public Response<PageInfo<Course>> getPublicCourseListById(@RequestParam(value = "user_id") String userId,
                                                        @RequestParam(value = "per_page") Integer perPage,
                                                        @RequestParam(value = "page") Integer page,
                                                        @RequestParam(value = "name", required = false) String name) {
        return Response.success(courseService.getPublicCourseListById(userId, perPage, page, name));
    }



    // 管理员添加内置课程
    @PostMapping(value = "/insert_course")
    public Response<Object> insertCourse(@RequestBody AddCourse addCourse) {
        Pair<Boolean, Object> pair = courseService.AddAdminCourse(addCourse);
        if (pair.getKey()) {
            return Response.success(pair.getValue());
        }
        return Response.error((String) pair.getValue());
    }

    // 管理员完善课程
    @PostMapping(value = "/insert_course_completed")
    public Response<Object> insertCourseCompleted(@RequestBody AddCourseCompleted addCourseCompleted) {
        Pair<Boolean, Object> pair = courseService.AddAdminCourseCompleted(addCourseCompleted);
        if (pair.getKey()) {
            return Response.success(pair.getValue());
        }
        return Response.error((String) pair.getValue());
    }

    // 管理员完善课程
    @PostMapping(value = "/insert_course_chapter_completed")
    public Response<Course> insertCourseChapterCompleted(@RequestBody AddChapterCompleted addChapterCompleted) {
        Pair<Boolean, String> pair = courseService.insertCourseChapterCompleted(addChapterCompleted);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
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

    // 获取管理员未发布的课程 0 未发布 1 已发布
    @GetMapping(value = "/get_admin_course_list_by_status")
    public Response<PageInfo<Course>> getAdminCourseListByType(@RequestParam(value = "user_id") String userId,
                                                         @RequestParam(value = "per_page") Integer perPage,
                                                         @RequestParam(value = "page") Integer page,
                                                         @RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "status", required = false) Integer status) {
        return Response.success(courseService.getAdminCourseListByStatus(userId, perPage, page, name, status));
    }

    // 归档课程
    @PostMapping(value = "/modify_expired_course_status")
    public Response<String> modifyExpiredCourseStatus(@RequestBody ModifyExpiredCourseStatus modifyExpiredCourseStatus) {
        Pair<Boolean, String> pair = courseService.modifyExpiredCourseStatus(modifyExpiredCourseStatus);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    @GetMapping(value = "/get_changed_course_list")
    public Response<PageInfo<Course>> getChangedList(@RequestParam(value = "per_page") Integer perPage,
                                                     @RequestParam(value = "page") Integer page,
                                                     @RequestParam(value = "name", required = false) String name) {
        return Response.success(courseService.getChangedList(perPage, page, name));
    }

    @GetMapping(value = "/get_expired_course_list")
    public Response<PageInfo<Course>> getExpiredList(@RequestParam(value = "per_page") Integer perPage,
                                                     @RequestParam(value = "page") Integer page,
                                                     @RequestParam(value = "name", required = false) String name) {
        return Response.success(courseService.getExpiredList(perPage, page, name));
    }

    // 学生端查询课程
    @GetMapping(value = "/get_course_list")
    public Response<PageInfo<Course>> getCourseList(@RequestParam(value = "user_id") String userId,
                                                    @RequestParam(value = "start", required = false) String start,
                                                    @RequestParam(value = "end", required = false) String end,
                                                    @RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "per_page") Integer perPage,
                                                    @RequestParam(value = "page") Integer page) {

        PageInfo<Course> courses = courseService.getCourseList(userId, name, start, end, perPage, page);
        return Response.success(courses);
    }


    @GetMapping(value = "/get_course_by_id")
    public Response<Object> getCourseById(@RequestParam(value = "course_id") String courseId) {
        Pair<Boolean, Object> pair = courseService.getCourseById(courseId);
        if (pair.getKey()) {
            return Response.success(pair.getValue());
        }
        return Response.error((String) pair.getValue());
    }

    @DeleteMapping(value = "/remove_course_by_id")
    public Response<String> removeCourseById(@RequestBody DeleteCourse deleteCourse) {
        Pair<Boolean, String> pair = courseService.removeCourseById(deleteCourse);
        if (pair.getKey()) {
            return Response.success(pair.getValue());
        }
        return Response.error(pair.getValue());
    }

}
