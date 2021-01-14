package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.body.Courseware;

import java.util.List;
import java.util.Map;

public interface CoursewareService {
    PageInfo<CoursewareEntity> getCoursewareByClassId(String classId, Integer perPage, Integer page);

    PageInfo<CoursewareEntity> getCoursewareBySectionId(String sectionId, Integer perPage,Integer page);

    PageInfo<CoursewareEntity> getCoursewareByChapterId(String chapterId, Integer perPage,Integer page);

    PageInfo<CoursewareEntity> getCoursewareByCourseId(String courseId, Integer perPage,Integer page);

    Map<String, Object> deleteCoursewareById(List<String> id);

    CoursewareEntity addCourseware(Courseware courseware);

    Boolean modifyCoursewareNameById(Courseware courseware);
}

