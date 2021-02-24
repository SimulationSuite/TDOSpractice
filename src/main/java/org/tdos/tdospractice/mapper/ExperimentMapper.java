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

    List<ExperimentEntity> findExperiment(@Param("category_ids")List<String> category_ids,@Param("name")String name);

    List<ExperimentEntity> findAllByIds(@Param("section_ids")List<String> section_ids);

    ExperimentEntity findById(String id);

    boolean updateExperiment(ExperimentEntity experimentEntity);

    boolean deleteExperiment(String id);

    int hasExperiment(String section_id);

    long deleteExperimentList(List<String> ids);

    int hasExperimentByName(@Param("name")String name);

}
