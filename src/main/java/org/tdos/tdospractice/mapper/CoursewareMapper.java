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
    List<CoursewareEntity> getCoursewareAll(@Param("name") String name, @Param("kind") String kind, @Param("type") String type, @Param("categoryId") String categoryId, @Param("cCategoryId") String cCategoryId, @Param("chapterId") String chapterId, @Param("sectionId") String sectionId);

    List<CoursewareEntity> getCoursewareByClassId(@Param("classId") String classId);

    List<CoursewareEntity> getCoursewareBySectionId(@Param("sectionId") String sectionId, @Param("kind") String kind, @Param("type") String type);

    List<CoursewareEntity> getCoursewareByChapterId(@Param("chapterId") String chapterId, @Param("kind") String kind, @Param("type") String type);

    List<CoursewareEntity> getCoursewareByCourseId(@Param("courseId") String courseId, @Param("kind") String kind, @Param("type") String type);

    List<ChapterSectionCoursewareEntity> getChapterSectionCoursewareByCourseId(@Param("courseId") String courseId);

    int deleteCoursewareById(@Param("id") String id);

    int deleteChapterSectionCourseById(@Param("chapterId") String chapterId, @Param("sectionId") String sectionId, @Param("coursewareId") String coursewareId);

    int modifyCoursewareNameById(@Param("id") String id, @Param("coursewareName") String coursewareName);

    int addCourseware(CoursewareEntity coursewareEntity);

    boolean ifSectionCourseware(String id);

    boolean ifSectionCoursewarePub(String id);

    int addChapterSectionCourseware(@Param("chapterSectionCoursewareEntityList") List<ChapterSectionCoursewareEntity> chapterSectionCoursewareEntityList);

    int hasSectionCoursewareId(String id);

    int hasChapterSectionCoursewareId(String coursewareId);

    int hasCoursewareMapperNameExist(@Param("name") String name);
}
