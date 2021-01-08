package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ExperimentEntity;

import java.util.List;

@Mapper
@Repository
public interface ExperimentMapper {
    int insert(ExperimentEntity experimentEntity);

    List<ExperimentEntity> findAllByCourseId(String course_id);

    List<ExperimentEntity> findAllByChapterId(String chapter_id);

    List<ExperimentEntity> findAllBySectionId(String section_id);

    List<ExperimentEntity> findAllByCategoryId(String category_id);

    ExperimentEntity findByID(String id);

    boolean updateExperiment(ExperimentEntity experimentEntity);

    boolean deleteExperiment(String id);

    long deleteExperimentList(List<String> ids);

}
