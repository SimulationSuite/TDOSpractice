package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.ExperimentReportEntity;
import org.tdos.tdospractice.mapper.ExperimentReportMapper;
import org.tdos.tdospractice.service.ExperimentReportService;

import java.util.List;
import java.util.Optional;

@Service
public class ExperimentReportServiceImpl implements ExperimentReportService {

    @Autowired
    private ExperimentReportMapper experimentReportMapper;

    @Override
    public int insert(ExperimentReportEntity experimentReportEntity) {
        return experimentReportMapper.insert(experimentReportEntity);
    }

    @Override
    public List<ExperimentReportEntity> findByExperimentReport(ExperimentReportEntity experimentReportEntity) {
        return experimentReportMapper.findByExperimentReport(experimentReportEntity);
    }

    @Override
    public boolean updateExperimentReport(ExperimentReportEntity experimentEntity) {
        return experimentReportMapper.updateExperimentReport(experimentEntity);
    }

    @Override
    public boolean updateExperimentReportByEndtime() {
        return experimentReportMapper.updateExperimentReportByEndtime();
    }

    @Override
    public Optional<ExperimentReportEntity> findExperimentReportByExperimentAndUserId(String experiment_id, String user_id) {
        return Optional.ofNullable(experimentReportMapper.findExperimentReportByExperimentAndUserId(experiment_id, user_id));
    }
}
