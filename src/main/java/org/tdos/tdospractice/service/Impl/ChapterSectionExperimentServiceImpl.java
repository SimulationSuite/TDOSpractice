package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.ChapterSectionExperimentEntity;
import org.tdos.tdospractice.mapper.ChapterSectionExperimentMapper;
import org.tdos.tdospractice.service.ChapterSectionExperimentService;

@Service
public class ChapterSectionExperimentServiceImpl implements ChapterSectionExperimentService {

    @Autowired
    private ChapterSectionExperimentMapper chapterSectionExperimentMapper;

    @Override
    public int insert(ChapterSectionExperimentEntity chapterSectionExperimentEntity) {
        return chapterSectionExperimentMapper.insert(chapterSectionExperimentEntity);
    }
}
