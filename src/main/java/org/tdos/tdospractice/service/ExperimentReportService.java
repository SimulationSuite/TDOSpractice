package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.ExperimentReportEntity;

import java.util.List;
import java.util.Optional;

public interface ExperimentReportService {

    int insert(ExperimentReportEntity experimentReportEntity);

    List<ExperimentReportEntity> findByExperimentReport(ExperimentReportEntity experimentReportEntity);

    boolean updateExperimentReport(ExperimentReportEntity experimentEntity);

    boolean updateExperimentReportByEndtime();

    Optional<ExperimentReportEntity> findExperimentReportByExperimentAndUserId(String experiment_id,String user_id);

}
