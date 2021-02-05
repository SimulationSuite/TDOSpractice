package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ExperimentEntity;
import org.tdos.tdospractice.entity.ExperimentImageEntity;

import java.util.List;

@Mapper
@Repository
public interface ExperimentImageMapper {

    int insertExperimentImages(List<ExperimentImageEntity> list);

    List<ExperimentImageEntity> findImageByExperiment(String experiment_id);
}
