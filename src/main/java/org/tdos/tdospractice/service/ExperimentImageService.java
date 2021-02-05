package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.ExperimentEntity;
import org.tdos.tdospractice.entity.ExperimentImageEntity;

import java.util.List;

public interface ExperimentImageService {

    int insertExperimentImages(List<ExperimentImageEntity> list);

    List<ExperimentImageEntity> findImageByExperiment(String experiment_id);

}
