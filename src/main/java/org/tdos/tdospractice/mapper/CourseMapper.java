package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.type.Course;

import java.util.List;

@Mapper
@Repository
public interface CourseMapper {

    List<Course> getAdminCourseList();

}
