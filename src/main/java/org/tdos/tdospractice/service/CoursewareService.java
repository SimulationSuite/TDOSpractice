package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.utils.OnlineStudent;

import java.util.List;
import java.util.Map;

public interface CoursewareService {
    List<CoursewareEntity> getCoursewareByClassId(String classId);

    List<CoursewareEntity> getCoursewareBySectionId(String sectionId);

    List<CoursewareEntity> getCoursewareByChapterId(String chapterId);

    List<CoursewareEntity> getCoursewareByCourseId(String courseId);

    Map<String, Object> deleteCoursewareById(List<String> id);

    CoursewareEntity addCourseware(String name, int type, int kind, String url);

    Boolean modifyCoursewareNameById(String id, String name);
}

