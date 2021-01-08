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

    Course getAdminCourseByCourseId(@Param("courseId") String courseId);

    int insertPrepareCourse(Course course);

    List<Course> getCourseListById(String userId);

    int findCourseChapterOrder(String courseId);
}
