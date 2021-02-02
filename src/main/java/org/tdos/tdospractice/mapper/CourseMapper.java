package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.type.Course;

import java.util.List;

@Mapper
@Repository
public interface CourseMapper {

    List<Course> getAdminCourseListByClassId(String classId);

    List<Course> getAdminCourseListByClassIdPerfect(@Param("courseIds") List<String> courseIds);

    List<Course> getAdminCourseList(@Param("name") String name);

    List<Course> getAdminCourseListPerfect(@Param("courseIds") List<String> courseIds);

    int hasCourseExist(String courseId);

    Course getCourseByCourseId(@Param("courseId") String courseId);

    int insertCourse(Course course);

    List<Course> getCourseListById(String userId, @Param("name") String name);

    List<Course> getCourseListByIdPerfect(@Param("courseIds") List<String> courseIds);

    Integer findCourseChapterOrder(String courseId);

    Integer findCourseChapterSectionOrder(String courseId, String chapterId);

    Integer findSmallSectionOrder(String sectionId);

    int modifyCourseStatus(String courseId, String start, String end);

    List<Course> getCourseList(String userId, String name, String start, String end);

    List<Course> getCourseListPerfect(@Param("courseIds") List<String> courseIds);

    List<Course> getAdminUnpublishedCourseList(String userId, String name);

    List<Course> getAdminUnpublishedCourseListPerfect(@Param("courseIds") List<String> courseIds);

    Course getCourseById(String courseId);

    List<Course> getExpiredList(@Param("name") String name);

    List<Course> getExpiredListPerfect(@Param("courseIds") List<String> courseIds);

    int hasCourseNameExist(String name);
}
