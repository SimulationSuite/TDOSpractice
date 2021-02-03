package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.CategoryEntity;
import org.tdos.tdospractice.entity.ExperimentEntity;
import org.tdos.tdospractice.mapper.ChapterMapper;
import org.tdos.tdospractice.mapper.ExperimentMapper;
import org.tdos.tdospractice.service.CategoryService;
import org.tdos.tdospractice.service.ChapterSectionExperimentService;
import org.tdos.tdospractice.service.CourseService;
import org.tdos.tdospractice.service.ExperimentService;
import org.tdos.tdospractice.type.Chapter;
import org.tdos.tdospractice.type.Course;
import org.tdos.tdospractice.type.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExperimentServiceImpl implements ExperimentService {


    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterSectionExperimentService chapterSectionExperimentService;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public int insert(ExperimentEntity experimentEntity) {
        return experimentMapper.insert(experimentEntity);
    }

    @Override
    public PageInfo<ExperimentEntity> findExperiment(List<String> category_ids, String name, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<ExperimentEntity> list = experimentMapper.findExperiment(category_ids, name);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ExperimentEntity> findAllByCourseId(String course_id, Integer perPage, Integer page) {
        Pair<Boolean, Object> pair = courseService.getCourseById(course_id);
        List<String> sectionid_list = new ArrayList<>();
        Course course = (Course) pair.getValue();
        course.chapters.forEach(chapter -> {
            chapter.getSections().forEach(section -> {
                sectionid_list.add(section.id);
            });
        });
        List<String> list = chapterSectionExperimentService.getExperimentIds(sectionid_list);
        return findAllByIds(list, perPage, page);
    }

    @Override
    public PageInfo<ExperimentEntity> findAllByChapterId(String chapter_id, Integer perPage, Integer page) {
        Chapter chapter = chapterMapper.getChapter(chapter_id);
        List<String> sectionid_list = new ArrayList<>();
        chapter.getSections().forEach(section -> {
            sectionid_list.add(section.id);
        });
        List<String> list = chapterSectionExperimentService.getExperimentIds(sectionid_list);
        return findAllByIds(list, perPage, page);
    }

    @Override
    public PageInfo<ExperimentEntity> findAllBySectionId(String section_id, Integer perPage, Integer page) {
        List<String> sectionid_list = new ArrayList<>();
        sectionid_list.add(section_id);
        List<String> list = chapterSectionExperimentService.getExperimentIds(sectionid_list);
        return findAllByIds(list, perPage, page);
    }

    @Override
    public PageInfo<ExperimentEntity> findAllByIds(List<String> section_ids, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        if (section_ids.size() == 0){
            return new PageInfo<>(new ArrayList<>());
        }
        List<ExperimentEntity> list = experimentMapper.findAllByIds(section_ids);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ExperimentEntity> findSelectedExperimentByCategory(String category_id, String section_id, String name, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<String> category_ids = new ArrayList<>();
        List<String> section_ids = new ArrayList<>();
        List<ExperimentEntity> list = experimentService.findExperiment(category_ids, name, perPage, page).getList();
        section_ids.add(section_id);
        if (category_id.equals("")) {
            experimentService.findExperiment(category_ids, name, perPage, page).getList().forEach(experimentEntity -> {
                experimentMapper.findAllByIds(section_ids).forEach(e -> {
                    if (e.getId().equals(experimentEntity.getId())) {
                        list.remove(experimentEntity);
                    }
                });
            });
        } else {
            Optional<CategoryEntity> categoryEntity = categoryService.findCategory(category_id);
            List<String> ids = new ArrayList<>();
            if (categoryEntity.isPresent()) {
                if (categoryEntity.get().getParent_category_id() == null) {
                    categoryService.findChildCategory(category_id).forEach(c -> {
                        ids.add(c.getId());
                    });
                } else {
                    ids.add(category_id);
                }
                experimentService.findExperiment(category_ids, name, perPage, page).getList().forEach(experimentEntity -> {
                    experimentMapper.findAllByIds(section_ids).forEach(e -> {
                        if (e.getId().equals(experimentEntity.getId())) {
                            list.remove(experimentEntity);
                        }
                    });
                });
            }
        }
        return new PageInfo<>(list);
    }

    @Override
    public ExperimentEntity findById(String id) {
        return experimentMapper.findById(id);
    }

    @Override
    public boolean updateExperiment(ExperimentEntity experimentEntity) {
        return experimentMapper.updateExperiment(experimentEntity);
    }

    @Override
    public boolean deleteExperiment(String id) {
        return experimentMapper.deleteExperiment(id);
    }

    @Override
    public int hasExperiment(String section_id) {
        return 0;
    }

    @Override
    public long deleteExperimentList(List<String> ids) {
        return experimentMapper.deleteExperimentList(ids);
    }
}
