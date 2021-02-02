package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import org.tdos.tdospractice.entity.ExperimentEntity;

import java.util.List;

public interface ExperimentService {

    int insert(ExperimentEntity experimentEntity);

    PageInfo<ExperimentEntity> findExperiment(List<String> category_ids, String name, Integer perPage, Integer page);

    PageInfo<ExperimentEntity> findAllByCourseId(String course_id, Integer perPage, Integer page);

    PageInfo<ExperimentEntity> findAllByChapterId(String chapter_id, Integer perPage, Integer page);

    PageInfo<ExperimentEntity> findAllBySectionId(String section_id, Integer perPage, Integer page);

    PageInfo<ExperimentEntity> findAllByIds(List<String> section_id, Integer perPage, Integer page);

    PageInfo<ExperimentEntity> findSelectedExperimentByCategory(String category_id, String section_id, String name, Integer perPage, Integer page);

    ExperimentEntity findById(String id);

    boolean updateExperiment(ExperimentEntity experimentEntity);

    boolean deleteExperiment(String id);

    int hasExperiment(String section_id);

    long deleteExperimentList(List<String> ids);
}
