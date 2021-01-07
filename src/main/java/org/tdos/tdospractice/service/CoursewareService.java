package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.utils.OnlineStudent;

import java.util.List;

public interface CoursewareService {
    List<CoursewareEntity> getCoursewareByClassId(String classId);

    List<CoursewareEntity> getCoursewareBySectionId(String sectionId);

    List<CoursewareEntity> getCoursewareByChapterId(String chapterId);

    List<CoursewareEntity> getCoursewareByCourseId(String courseId);

    int deleteCoursewareById(String id);

    CoursewareEntity addCourseware(String name, int type, int kind, String url);

    int ifSectionCoursewareByCoursewareId(String id);

    int modifyCoursewareNameById(String id, String name);
}

