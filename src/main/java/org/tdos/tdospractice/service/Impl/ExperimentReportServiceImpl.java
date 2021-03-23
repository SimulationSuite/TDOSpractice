package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.ExperimentReportEntity;
import org.tdos.tdospractice.mapper.ExperimentReportMapper;
import org.tdos.tdospractice.service.ExperimentReportService;
import org.tdos.tdospractice.type.AllExperimentReport;

import java.time.LocalDateTime;
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
        return experimentReportMapper.updateExperimentReportByEndtime(LocalDateTime.now());
    }

    @Override
    public Optional<ExperimentReportEntity> findExperimentReportByExperimentAndUserId(String experiment_id, String user_id) {
        return Optional.ofNullable(experimentReportMapper.findExperimentReportByExperimentAndUserId(experiment_id, user_id));
    }

    @Override
    public PageInfo<AllExperimentReport> findExperimentReportAll(String courseId, int status, int isCorrect, String name, String startTime, String endTime, String ownerId, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<AllExperimentReport> list = experimentReportMapper.findExperimentReportAll(courseId, status, isCorrect, name, startTime, endTime, ownerId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AllExperimentReport> findStudentExperimentReport(String course_id, int status, String name,String user_id, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<AllExperimentReport> list = experimentReportMapper.findStudentExperimentReport(course_id,status,name,user_id);
        return new PageInfo<>(list);
    }
}
