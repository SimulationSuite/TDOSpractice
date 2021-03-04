package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ExperimentReportEntity;
import org.tdos.tdospractice.type.AllExperimentReport;

import java.util.List;


@Mapper
@Repository
public interface ExperimentReportMapper {
    int insert(ExperimentReportEntity experimentReportEntity);

    List<ExperimentReportEntity> findByExperimentReport(ExperimentReportEntity experimentReportEntity);

    boolean updateExperimentReport(ExperimentReportEntity experimentEntity);

    boolean updateExperimentReportByEndtime();

    ExperimentReportEntity findExperimentReportByExperimentAndUserId(@Param("experiment_id") String experiment_id, @Param("user_id") String user_id);

    List<AllExperimentReport> findExperimentReportAll(@Param("course_id") String course_id, @Param("status") int status, @Param("isCorrect") int isCorrect, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("ownerId") String ownerId);

    List<AllExperimentReport> findStudentExperimentReport(@Param("course_id") String course_id, @Param("isCorrect") int isCorrect, @Param("name") String name, @Param("user_id") String user_id);

}
