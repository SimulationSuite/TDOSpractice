package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.CoursewareEntity;

import java.util.List;

@Mapper
@Repository
public interface CoursewareMapper {
    List<CoursewareEntity> getCoursewareByClassId(@Param("classId") String classId);

    List<CoursewareEntity> getCoursewareBySectionId(@Param("sectionId") String sectionId);

    List<CoursewareEntity> getCoursewareByChapterId(@Param("chapterId") String chapterId);

    List<CoursewareEntity> getCoursewareByCourseId(@Param("courseId") String courseId);

    int deleteCoursewareById(@Param("id") String id);

    int modifyCoursewareNameById(@Param("id") String id, @Param("coursewareName") String coursewareName);

    int addCourseware(CoursewareEntity coursewareEntity);

    int ifSectionCourseware(String id);
}
