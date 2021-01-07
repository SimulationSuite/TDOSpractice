package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.mapper.CoursewareMapper;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.service.CoursewareService;

import java.util.List;

@Service
public class CoursewareServiceImpl extends Throwable implements CoursewareService {

    @Autowired
    private CoursewareMapper coursewareMapper;

    @Override
    public List<CoursewareEntity> getCoursewareByClassId(String classId) {
        return coursewareMapper.getCoursewareByClassId(classId);
    }

    @Override
    public List<CoursewareEntity> getCoursewareBySectionId(String sectionId) {
        return coursewareMapper.getCoursewareBySectionId(sectionId);
    }

    @Override
    public List<CoursewareEntity> getCoursewareByChapterId(String chapterId) {
        return coursewareMapper.getCoursewareByChapterId(chapterId);
    }

    @Override
    public List<CoursewareEntity> getCoursewareByCourseId(String courseId) {
        return coursewareMapper.getCoursewareByCourseId(courseId);
    }

    @Override
    public int deleteCoursewareById(String id) {
        int i = 0;
        try {
            i = coursewareMapper.deleteCoursewareById(id);
        } catch (Exception e) {
            return i;
        }
        return i;
    }

    @Override
    public CoursewareEntity addCourseware(String name, int type, int kind, String url) {
        CoursewareEntity coursewareEntity = new CoursewareEntity();
        coursewareEntity.setName(name);
        coursewareEntity.setKind(kind);
        coursewareEntity.setType(type);
        coursewareEntity.setUrl(url);
        try {
            int i = coursewareMapper.addCourseware(coursewareEntity);
        } catch (Exception e) {
            return coursewareEntity;
        }
        return coursewareEntity;
    }

    @Override
    public int ifSectionCoursewareByCoursewareId(String id) {
        int i = 0;
        try {
            i = coursewareMapper.ifSectionCoursewareByCoursewareId(id);
        } catch (Exception e) {
            return i;
        }
        return i;
    }

    @Override
    public int modifyCoursewareNameById(String id, String name) {
        int i = 0;
        try {
            i = coursewareMapper.modifyCoursewareNameById(id, name);
        } catch (Exception e) {
            return i;
        }
        return i;
    }

}
