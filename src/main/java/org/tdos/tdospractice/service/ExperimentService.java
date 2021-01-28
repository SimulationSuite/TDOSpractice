package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.ExperimentEntity;

import java.util.List;

public interface ExperimentService {

    int insert(ExperimentEntity experimentEntity);

    List<ExperimentEntity> findExperiment(List<String> category_ids,String name);

    List<ExperimentEntity> findAllByCourseId(String course_id);

    List<ExperimentEntity> findAllByChapterId(String chapter_id);

    List<ExperimentEntity> findAllBySectionId(String section_id);

    List<ExperimentEntity> findAllByCategoryId(String category_id);

    ExperimentEntity findByID(String id);

    boolean updateExperiment(ExperimentEntity experimentEntity);

    boolean deleteExperiment(String id);

    long deleteExperimentList(List<String> ids);
}
