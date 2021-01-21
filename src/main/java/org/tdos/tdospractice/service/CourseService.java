package org.tdos.tdospractice.service;


import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.tdos.tdospractice.body.AddCourse;
import org.tdos.tdospractice.body.ModifyCourseStatus;
import org.tdos.tdospractice.body.PrepareCourse;
import org.tdos.tdospractice.type.Course;

public interface CourseService {

    PageInfo<Course> getAdminCourseList(Integer perPage, Integer page, String name);

    PageInfo<Course> getAdminCourseListByClassId(String classId, Integer perPage, Integer page);

    Pair<Boolean, String> prepareCourse(PrepareCourse prepareCourse);

    PageInfo<Course> getCourseListById(String userId, Integer perPage, Integer page,String name);

    Course AddAdminCourse(AddCourse addCourse);

    Pair<Boolean, String> modifyCourseStatus(ModifyCourseStatus modifyCourseStatus);

    PageInfo<Course> getAdminUnpublishedCourseList(String userId, Integer perPage, Integer page, String name);

    PageInfo<Course> getCourseList(String userId, String start, String end, Integer perPage, Integer page);

    Course getCourseById(String courseId);

    PageInfo<Course> getExpiredList(Integer perPage, Integer page);
}
