package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.tdos.tdospractice.body.ChapterSectionCoursewareList;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.entity.ChapterSectionCoursewareEntity;
import org.tdos.tdospractice.entity.QuestionBackAssignmentEntity;
import org.tdos.tdospractice.mapper.CoursewareMapper;
import org.tdos.tdospractice.service.CoursewareService;
import org.tdos.tdospractice.body.Courseware;
import org.tdos.tdospractice.body.ChapterSectionCourseware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoursewareServiceImpl extends Throwable implements CoursewareService {

    @Autowired
    private CoursewareMapper coursewareMapper;

    @Override
    public PageInfo<CoursewareEntity> getCoursewareAll(String name, Integer kind, Integer type, String categoryId,String cCategoryId, String chapterId, String sectionId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<CoursewareEntity> list = coursewareMapper.getCoursewareAll(name, kind, type, categoryId,cCategoryId, chapterId, sectionId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<CoursewareEntity> getCoursewareByClassId(String classId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<CoursewareEntity> list = coursewareMapper.getCoursewareByClassId(classId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<CoursewareEntity> getCoursewareBySectionId(String sectionId, Integer kind, Integer type, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<CoursewareEntity> list = coursewareMapper.getCoursewareBySectionId(sectionId, kind, type);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<CoursewareEntity> getCoursewareByChapterId(String chapterId, Integer kind, Integer type, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<CoursewareEntity> list = coursewareMapper.getCoursewareByChapterId(chapterId, kind, type);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<CoursewareEntity> getCoursewareByCourseId(String courseId, Integer kind, Integer type, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<CoursewareEntity> list = coursewareMapper.getCoursewareByCourseId(courseId, kind, type);
        return new PageInfo<>(list);
    }

    @Override
    public Map<String, Object> deleteCoursewareById(List<String> id) {
        Map<String, Object> map = new HashMap<>();
        List<String> sectionCourseware = new ArrayList<String>();
        id.forEach(x -> {
            if (coursewareMapper.hasChapterSectionCoursewareId(x) > 0){
                sectionCourseware.add(x);
            }
        });
        if (sectionCourseware.size() > 0){
            map.put("isDelete", false);
            map.put("canNotDeleteId", sectionCourseware);
            map.put("reason", "课件已关联到章节。");
            return map;
        }
        id.forEach(x -> {
            coursewareMapper.deleteCoursewareById(x);
        });
        map.put("isDelete", true);
        map.put("deleteId", sectionCourseware);
        return map;
    }

    @Override
    public Map<String, Object> deleteChapterSectionCourseById(List<ChapterSectionCourseware> chapterSectionCoursewareList) {
        Map<String, Object> map = new HashMap<>();
        List<ChapterSectionCourseware> deleteChapterSectionCoursewareList = new ArrayList<ChapterSectionCourseware>();
        chapterSectionCoursewareList.forEach(x -> {
            coursewareMapper.deleteChapterSectionCourseById(x.chapterId, x.sectionId, x.coursewareId);
            deleteChapterSectionCoursewareList.add(x);
        });
        map.put("isDelete", true);
        map.put("deleteId", deleteChapterSectionCoursewareList);
        return map;
    }

    @Override
    public Pair<Boolean, Object> addCourseware(Courseware courseware) {
        if (ObjectUtils.isEmpty(courseware.name)) {
            return new Pair<>(false, "课件名称不能为空。");
        }
        if (coursewareMapper.hasCoursewareMapperNameExist(courseware.name) > 0) {
            return new Pair<>(false, "课件名称已存在。");
        }
        if (ObjectUtils.isEmpty(courseware.type)) {
            return new Pair<>(false, "课件类型不能为空。");
        }
        if (ObjectUtils.isEmpty(courseware.kind)) {
            return new Pair<>(false, "课件种类不能为空。");
        }
        if (ObjectUtils.isEmpty(courseware.url)) {
            return new Pair<>(false, "课件链接地址不能为空。");
        }
        if (ObjectUtils.isEmpty(courseware.size)) {
            return new Pair<>(false, "课件大小不能为空。");
        }
        if (ObjectUtils.isEmpty(courseware.categoryId)) {
            return new Pair<>(false, "课件分类不能为空。");
        }
        CoursewareEntity coursewareEntity = new CoursewareEntity();
        coursewareEntity.setName(courseware.name);
        coursewareEntity.setKind(courseware.kind);
        coursewareEntity.setType(courseware.type);
        coursewareEntity.setUrl(courseware.url);
        coursewareEntity.setDuration(courseware.duration);
        coursewareEntity.setSize(courseware.size);
        coursewareEntity.setCategoryId(courseware.categoryId);
        try {
            coursewareMapper.addCourseware(coursewareEntity);
        } catch (Exception e) {
            return new Pair<>(false, e);
        }
        return new Pair<>(true, coursewareEntity);
    }

    @Override
    public List<ChapterSectionCoursewareEntity> addChapterSectionCourseware(List<ChapterSectionCourseware> chapterSectionCoursewareList) {
        List<ChapterSectionCoursewareEntity> chapterSectionCoursewareEntityList = new ArrayList<ChapterSectionCoursewareEntity>();
        chapterSectionCoursewareList.forEach(x -> {
            ChapterSectionCoursewareEntity chapterSectionCoursewareEntity = new ChapterSectionCoursewareEntity();
            chapterSectionCoursewareEntity.setChapterId(x.chapterId);
            chapterSectionCoursewareEntity.setSectionId(x.sectionId);
            chapterSectionCoursewareEntity.setCoursewareId(x.coursewareId);
            chapterSectionCoursewareEntityList.add(chapterSectionCoursewareEntity);
        });
        try {
            coursewareMapper.addChapterSectionCourseware(chapterSectionCoursewareEntityList);
        } catch (Exception e) {
            return chapterSectionCoursewareEntityList;
        }
        return chapterSectionCoursewareEntityList;
    }

    @Override
    public Map<String, Object> modifyCoursewareNameById(Courseware courseware) {
        Map<String, Object> map = new HashMap<>();
        try {
            coursewareMapper.modifyCoursewareNameById(courseware.id, courseware.name);
        } catch (Exception e) {
            map.put("isModify", false);
            map.put("reason", e);
            return map;
        }
        map.put("isModify", true);
        return map;
    }

}
