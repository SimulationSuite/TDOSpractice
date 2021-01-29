package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.ChapterSectionExperimentEntity;
import org.tdos.tdospractice.mapper.ChapterSectionExperimentMapper;
import org.tdos.tdospractice.service.ChapterSectionExperimentService;

import java.util.List;

@Service
public class ChapterSectionExperimentServiceImpl implements ChapterSectionExperimentService {

    @Autowired
    private ChapterSectionExperimentMapper chapterSectionExperimentMapper;

    @Override
    public int insert(List<ChapterSectionExperimentEntity> list) {
        return chapterSectionExperimentMapper.insert(list);
    }
}
