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
import org.tdos.tdospractice.mapper.ChapterSectionExperimentMapper;
import org.tdos.tdospractice.mapper.ExperimentImageMapper;
import org.tdos.tdospractice.mapper.ExperimentMapper;
import org.tdos.tdospractice.service.*;
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

    @Autowired
    private ExperimentImageService experimentImageService;

    @Autowired
    private ChapterSectionExperimentMapper chapterSectionExperimentMapper;

    @Override
    public int insert(ExperimentEntity experimentEntity) {
        if (experimentMapper.hasExperimentByName(experimentEntity.getName()) == 0){
            return experimentMapper.insert(experimentEntity);
        }else {
            return -1;
        }
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
        if (sectionid_list.size() == 0) {
            return new PageInfo<>(new ArrayList<>());
        }
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
        if (sectionid_list.size() == 0) {
            return new PageInfo<>(new ArrayList<>());
        }
        List<String> list = chapterSectionExperimentService.getExperimentIds(sectionid_list);
        return findAllByIds(list, perPage, page);
    }

    @Override
    public PageInfo<ExperimentEntity> findAllBySectionId(String section_id, Integer perPage, Integer page) {
        List<String> sectionid_list = new ArrayList<>();
        sectionid_list.add(section_id);
        if (sectionid_list.size() == 0) {
            return new PageInfo<>(new ArrayList<>());
        }
        List<String> list = chapterSectionExperimentService.getExperimentIds(sectionid_list);
        return findAllByIds(list, perPage, page);
    }

    @Override
    public PageInfo<ExperimentEntity> findAllByIds(List<String> section_ids, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        if (section_ids.size() == 0) {
            return new PageInfo<>(new ArrayList<>());
        }
        List<ExperimentEntity> list = experimentMapper.findAllByIds(section_ids);
        list.stream().forEach(experimentEntity -> {
            experimentEntity.setImagesinfo(experimentImageService.findImageByExperiment(experimentEntity.getId()));
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ExperimentEntity> findSelectedExperimentByCategory(String f_category_id, String c_category_id, String section_id, String name, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<String> category_ids = new ArrayList<>();
        List<String> section_ids = new ArrayList<>();
        List<ExperimentEntity> list = experimentService.findExperiment(category_ids, name, perPage, page).getList();
        section_ids.add(section_id);
        if (f_category_id.equals("") && c_category_id.equals("")) {
            experimentService.findExperiment(category_ids, name, perPage, page).getList().forEach(experimentEntity -> {
                chapterSectionExperimentMapper.getChapterSectionExperimentBySection(section_id).stream().forEach(chapterSectionExperimentEntity -> {
                    if (experimentEntity.getId().equals(chapterSectionExperimentEntity.getExperiment_id())){
                        list.remove(experimentEntity);
                    }
                });

            });
        } else {
            List<String> ids = new ArrayList<>();
            if (c_category_id.equals("")) {
                categoryService.findChildCategory(f_category_id).forEach(c -> {
                    ids.add(c.getId());
                });
            } else {
                ids.add(c_category_id);
            }
            experimentService.findExperiment(ids, name, perPage, page).getList().forEach(experimentEntity -> {
//                experimentMapper.findAllByIds(section_ids).forEach(e -> {
//                    if (e.getId().equals(experimentEntity.getId())) {
//                        list.remove(experimentEntity);
//                    }
//                });
                chapterSectionExperimentMapper.getChapterSectionExperimentBySection(section_id).stream().forEach(chapterSectionExperimentEntity -> {
                    if (experimentEntity.getId().equals(chapterSectionExperimentEntity.getExperiment_id())){
                        list.remove(experimentEntity);
                    }
                });
            });
        }
        return new PageInfo<>(list);
    }

    @Override
    public ExperimentEntity findById(String id) {
        ExperimentEntity experimentEntity = experimentMapper.findById(id);
        experimentEntity.setImagesinfo(experimentImageService.findImageByExperiment(id));
        return experimentEntity;
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
