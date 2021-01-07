package org.tdos.tdospractice.service;

import org.apache.ibatis.annotations.Param;
import org.tdos.tdospractice.entity.ExperimentEntity;

import java.util.List;

public interface ExperimentService {

    int insert(ExperimentEntity experimentEntity);

    List<ExperimentEntity> findAllByCourseId(String course_id);

    List<ExperimentEntity> findAllByChapterId(String chapter_id);

    List<ExperimentEntity> findAllBySectionId(String section_id);

    List<ExperimentEntity> findAllByCategoryId(String category_id);

    boolean updateExperiment(ExperimentEntity experimentEntity);

    boolean deleteExperiment(String id);

    long deleteExperimentList(List<String> ids);
}
