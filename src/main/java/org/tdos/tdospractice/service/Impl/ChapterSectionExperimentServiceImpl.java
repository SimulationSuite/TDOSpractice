package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageInfo;
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

    @Override
    public List<String> getExperimentIds(List<String> section_ids) {
        return chapterSectionExperimentMapper.getExperimentIds(section_ids);
    }
}
