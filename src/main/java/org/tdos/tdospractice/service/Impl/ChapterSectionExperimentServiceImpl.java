package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
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

    @Override
    public Pair<Boolean, String> deleteChapterSectionExperiment(String id) {
        boolean b = chapterSectionExperimentMapper.deleteChapterSectionExperiment(id);
        if (b){
            return new Pair<>(b,"删除成功");
        }else {
            return new Pair<>(b,"删除失败");
        }
    }

    @Override
    public int getExperimentNumberbySection(String section_id) {
        return chapterSectionExperimentMapper.getExperimentNumberbySection(section_id);
    }

    @Override
    public int getSectionNumberbyExperiment(String experiment_id) {
        return chapterSectionExperimentMapper.getSectionNumberbyExperiment(experiment_id);
    }
}
