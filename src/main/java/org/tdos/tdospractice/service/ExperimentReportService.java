package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import org.tdos.tdospractice.entity.ExperimentReportEntity;
import org.tdos.tdospractice.type.StudentExperimentReport;

import java.util.List;
import java.util.Optional;

public interface ExperimentReportService {

    int insert(ExperimentReportEntity experimentReportEntity);

    List<ExperimentReportEntity> findByExperimentReport(ExperimentReportEntity experimentReportEntity);

    boolean updateExperimentReport(ExperimentReportEntity experimentEntity);

    boolean updateExperimentReportByEndtime();

    Optional<ExperimentReportEntity> findExperimentReportByExperimentAndUserId(String experiment_id, String user_id);

    PageInfo<StudentExperimentReport> findExperimentReportAll(String course_id, int status, int isCorrect, String name, String startTime, String endTime, Integer perPage, Integer page);

}
