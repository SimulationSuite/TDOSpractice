package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.mapper.CoursewareMapper;
import org.tdos.tdospractice.service.CoursewareService;
import org.tdos.tdospractice.body.Courseware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoursewareServiceImpl extends Throwable implements CoursewareService {

    @Autowired
    private CoursewareMapper coursewareMapper;

    @Override
    public PageInfo<CoursewareEntity> getCoursewareByClassId(String classId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<CoursewareEntity> list = coursewareMapper.getCoursewareByClassId(classId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<CoursewareEntity> getCoursewareBySectionId(String sectionId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<CoursewareEntity> list = coursewareMapper.getCoursewareBySectionId(sectionId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<CoursewareEntity> getCoursewareByChapterId(String chapterId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<CoursewareEntity> list = coursewareMapper.getCoursewareByChapterId(chapterId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<CoursewareEntity> getCoursewareByCourseId(String courseId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<CoursewareEntity> list = coursewareMapper.getCoursewareByCourseId(courseId);
        return new PageInfo<>(list);
    }

    @Override
    public Map<String, Object> deleteCoursewareById(List<String> id) {
        Map<String, Object> map = new HashMap<>();
        List<String> sectionCourseware = new ArrayList<String>();
        id.forEach(x -> {
            if (!coursewareMapper.ifSectionCourseware(x)){
                sectionCourseware.add(x);
            }
        });
        if (sectionCourseware.size() > 0){
            map.put("isDelete", false);
            map.put("notDeleteId", sectionCourseware);
            return map;
        }
        id.forEach(x -> coursewareMapper.deleteCoursewareById(x));
        map.put("isDelete", true);
        map.put("notDeleteId", sectionCourseware);
        return map;
    }

    @Override
    public CoursewareEntity addCourseware(Courseware courseware) {
        CoursewareEntity coursewareEntity = new CoursewareEntity();
        coursewareEntity.setName(courseware.name);
        coursewareEntity.setKind(courseware.kind);
        coursewareEntity.setType(courseware.type);
        coursewareEntity.setUrl(courseware.url);
        try {
            coursewareMapper.addCourseware(coursewareEntity);
        } catch (Exception e) {
            return coursewareEntity;
        }
        return coursewareEntity;
    }

    @Override
    public Boolean modifyCoursewareNameById(Courseware courseware) {
        try {
            coursewareMapper.modifyCoursewareNameById(courseware.id, courseware.name);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
