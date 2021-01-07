package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.mapper.CoursewareMapper;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.service.CoursewareService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<Boolean, List<String>> deleteCoursewareById(List<String> id) {
        Map<Boolean, List<String>> map = new HashMap<>();
        List<String> sectionCourseware = new ArrayList<String>();
        id.forEach(x -> {
            int i = coursewareMapper.ifSectionCourseware(x);
            if (i == 1){
                sectionCourseware.add(x);
            }
        });
        if (sectionCourseware.size() > 0){
            map.put(false, sectionCourseware);
            return map;
        }
        id.forEach(x -> coursewareMapper.deleteCoursewareById(x));
        map.put(true, sectionCourseware);
        return map;
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
