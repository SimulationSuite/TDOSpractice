package org.tdos.tdospractice.service;


import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.tdos.tdospractice.body.*;
import org.tdos.tdospractice.type.Course;

public interface CourseService {

    // 1
    PageInfo<Course> getAdminCourseList(Integer perPage, Integer page, String name);

    PageInfo<Course> getAdminCourseListByClassId(String classId, Integer perPage, Integer page);

    Pair<Boolean, String> prepareCourse(PrepareCourse prepareCourse);

    PageInfo<Course> getCourseListById(String userId, Integer perPage, Integer page,String name);

    Pair<Boolean, Object> AddAdminCourse(AddCourse addCourse);

    Pair<Boolean, String> modifyCourseStatus(ModifyCourseStatus modifyCourseStatus);
    // 1
    PageInfo<Course> getAdminUnpublishedCourseList(String userId, Integer perPage, Integer page, String name);
    // 1
    PageInfo<Course> getCourseList(String userId, String start, String end, Integer perPage, Integer page);

    Pair<Boolean, Object> getCourseById(String courseId);
    // 1
    PageInfo<Course> getExpiredList(Integer perPage, Integer page, String name);

    Pair<Boolean, Object> AddAdminCourseCompleted(AddCourseCompleted addCourseCompleted);

    Pair<Boolean, String> insertCourseChapterCompleted(AddChapterCompleted addChapterCompleted);
}
