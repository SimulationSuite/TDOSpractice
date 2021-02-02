package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.entity.SectionCoursewareEntity;
import org.tdos.tdospractice.entity.ChapterSectionCoursewareEntity;

import java.util.List;

@Mapper
@Repository
public interface CoursewareMapper {
    List<CoursewareEntity> getCoursewareAll(@Param("name") String name, @Param("kind") Integer kind, @Param("type") Integer type, @Param("categoryId") String categoryId, @Param("chapterId") String chapterId, @Param("sectionId") String sectionId);

    List<CoursewareEntity> getCoursewareByClassId(@Param("classId") String classId);

    List<CoursewareEntity> getCoursewareBySectionId(@Param("sectionId") String sectionId, @Param("kind") Integer kind, @Param("type") Integer type);

    List<CoursewareEntity> getCoursewareByChapterId(@Param("chapterId") String chapterId, @Param("kind") Integer kind, @Param("type") Integer type);

    List<CoursewareEntity> getCoursewareByCourseId(@Param("courseId") String courseId, @Param("kind") Integer kind, @Param("type") Integer type);

    int deleteCoursewareById(@Param("id") String id);

    int deleteChapterSectionCourseById(@Param("chapterId") String chapterId, @Param("sectionId") String sectionId, @Param("coursewareId") String coursewareId);

    int modifyCoursewareNameById(@Param("id") String id, @Param("coursewareName") String coursewareName);

    int addCourseware(CoursewareEntity coursewareEntity);

    boolean ifSectionCourseware(String id);

    boolean ifSectionCoursewarePub(String id);

    int addChapterSectionCourseware(ChapterSectionCoursewareEntity chapterSectionCoursewareEntity);

    int hasSectionCoursewareId(String id);

    int hasChapterSectionCoursewareId(String coursewareId);
}
