package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ClassCourse;

import java.util.List;

@Mapper
@Repository
public interface ClassCourseMapper {

    int insertClassCourse(String userId, String courseId, String classId);

    int deleteByCourseId(String courseId);

    List<ClassCourse> findListByCourseId(String courseId);

}