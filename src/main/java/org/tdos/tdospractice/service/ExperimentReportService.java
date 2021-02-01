package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.ExperimentReportEntity;

import java.util.List;

public interface ExperimentReportService {

    int insert(ExperimentReportEntity experimentReportEntity);

    List<ExperimentReportEntity> findByExperimentReport(ExperimentReportEntity experimentReportEntity);

    boolean updateExperimentReport(ExperimentReportEntity experimentEntity);

    boolean updateExperimentReportByEndtime();
}
