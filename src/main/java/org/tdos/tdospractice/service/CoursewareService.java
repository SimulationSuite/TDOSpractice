package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.body.Courseware;
import org.tdos.tdospractice.entity.ChapterSectionCoursewareEntity;
import org.tdos.tdospractice.body.ChapterSectionCourseware;

import java.util.List;
import java.util.Map;

public interface CoursewareService {
    PageInfo<CoursewareEntity> getCoursewareAll(String name, Integer kind, Integer type, String categoryId, Integer perPage, Integer page);

    PageInfo<CoursewareEntity> getCoursewareByClassId(String classId, Integer perPage, Integer page);

    PageInfo<CoursewareEntity> getCoursewareBySectionId(String sectionId, Integer perPage,Integer page);

    PageInfo<CoursewareEntity> getCoursewareByChapterId(String chapterId, Integer perPage,Integer page);

    PageInfo<CoursewareEntity> getCoursewareByCourseId(String courseId, Integer perPage,Integer page);

    Map<String, Object> deleteCoursewareById(List<String> id);

    CoursewareEntity addCourseware(Courseware courseware);

    Map<String, Object> modifyCoursewareNameById(Courseware courseware);

    ChapterSectionCoursewareEntity addChapterSectionCourseware(ChapterSectionCourseware chapterSectionCourseware);
}

