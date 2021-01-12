package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.type.Course;

import java.util.List;

@Mapper
@Repository
public interface CourseMapper {

    List<Course> getAdminCourseList();

    int hasCourseExist(String courseId);

    Course getCourseByCourseId(@Param("courseId") String courseId);

    int insertCourse(Course course);

    List<Course> getCourseListById(String userId);

    Integer findCourseChapterOrder(String courseId);

    Integer findCourseChapterSectionOrder(String courseId,String chapterId);

    int modifyCourseStatus(String courseId);

    List<Course> getCourseList(String userId);
}
