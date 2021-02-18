package org.tdos.tdospractice.service;


import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.tdos.tdospractice.body.*;
import org.tdos.tdospractice.type.Course;

public interface CourseService {

    // 1
    PageInfo<Course> getAdminCourseList(Integer perPage, Integer page, String name);

    Pair<Boolean, Object> getAdminCourseListByClassId(String classId, Integer perPage, Integer page);

    Pair<Boolean, String> prepareCourse(PrepareCourse prepareCourse);

    PageInfo<Course> getCourseListById(String userId, Integer perPage, Integer page,String name);

    Pair<Boolean, Object> AddAdminCourse(AddCourse addCourse);

    Pair<Boolean, String> modifyCourseStatus(ModifyCourseStatus modifyCourseStatus);
    // 1
    PageInfo<Course> getAdminCourseListByStatus(String userId, Integer perPage, Integer page, String name, Integer status);
    // 1
    PageInfo<Course> getCourseList(String userId,String name, String start, String end, Integer perPage, Integer page);

    Pair<Boolean, Object> getCourseById(String courseId);
    // 1
    PageInfo<Course> getExpiredList(Integer perPage, Integer page, String name);

    Pair<Boolean, Object> AddAdminCourseCompleted(AddCourseCompleted addCourseCompleted);

    Pair<Boolean, String> insertCourseChapterCompleted(AddChapterCompleted addChapterCompleted);

    Pair<Boolean, String> modifyExpiredCourseStatus(ModifyExpiredCourseStatus modifyExpiredCourseStatus);

    PageInfo<Course> getChangedList(Integer perPage, Integer page, String name);

    Pair<Boolean, String> removeCourseById(DeleteCourse deleteCourse);
}
