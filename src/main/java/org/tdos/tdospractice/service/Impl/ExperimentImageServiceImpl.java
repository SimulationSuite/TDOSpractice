package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.ExperimentImageEntity;
import org.tdos.tdospractice.mapper.ExperimentImageMapper;
import org.tdos.tdospractice.service.ExperimentImageService;

import java.util.List;

@Service
public class ExperimentImageServiceImpl implements ExperimentImageService {

    @Autowired
    private ExperimentImageMapper experimentImageMapper;

    @Override
    public int insertExperimentImages(List<ExperimentImageEntity> list) {
        return experimentImageMapper.insertExperimentImages(list);
    }
}
