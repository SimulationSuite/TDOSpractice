package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.type.Course;

import java.util.List;

@Mapper
@Repository
public interface CourseMapper {

    List<Course> getAdminCourseListByClassId(String classId);

    List<Course> getAdminCourseList(@Param("name") String name);

    int hasCourseExist(String courseId);

    Course getCourseByCourseId(@Param("courseId") String courseId);

    int insertCourse(Course course);

    List<Course> getCourseListById(String userId);

    Integer findCourseChapterOrder(String courseId);

    Integer findCourseChapterSectionOrder(String courseId, String chapterId);

    int modifyCourseStatus(String courseId,String start,String end);

    List<Course> getCourseList(String userId, String start, String end);

    List<Course> getAdminUnpublishedCourseList();

    Course getCourseById(String courseId);
}
