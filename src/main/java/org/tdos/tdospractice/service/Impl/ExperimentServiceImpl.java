package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.ExperimentEntity;
import org.tdos.tdospractice.mapper.ExperimentMapper;
import org.tdos.tdospractice.service.ExperimentService;

import java.util.List;

@Service
public class ExperimentServiceImpl implements ExperimentService {


    @Autowired
    private ExperimentMapper experimentMapper;

    @Override
    public int insert(ExperimentEntity experimentEntity) {
        return experimentMapper.insert(experimentEntity);
    }

    @Override
    public PageInfo<ExperimentEntity> findExperiment(List<String> category_ids, String name, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<ExperimentEntity> list = experimentMapper.findExperiment(category_ids,name);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ExperimentEntity> findAllByCourseId(String course_id, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<ExperimentEntity> list = experimentMapper.findAllByCourseId(course_id);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ExperimentEntity> findAllByChapterId(String chapter_id, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<ExperimentEntity> list = experimentMapper.findAllByChapterId(chapter_id);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ExperimentEntity> findAllBySectionId(String section_id, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<ExperimentEntity> list = experimentMapper.findAllBySectionId(section_id);
        return new PageInfo<>(list);
    }


    @Override
    public List<ExperimentEntity> findAllByCategoryId(String category_id) {
        return experimentMapper.findAllByCategoryId(category_id);
    }

    @Override
    public ExperimentEntity findByID(String id) {
        return experimentMapper.findByID(id);
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
    public long deleteExperimentList(List<String> ids) {
        return experimentMapper.deleteExperimentList(ids);
    }
}
