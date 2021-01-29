package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ExperimentReportEntity;

import java.util.List;


@Mapper
@Repository
public interface ExperimentReportMapper {
    int insert(ExperimentReportEntity experimentReportEntity);

    List<ExperimentReportEntity> findByExperimentReport(ExperimentReportEntity experimentReportEntity);

    boolean updateExperimentReport(ExperimentReportEntity experimentEntity);

}
