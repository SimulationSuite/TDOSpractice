package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.tdos.tdospractice.entity.ChapterSectionExperimentEntity;

import java.util.List;

public interface ChapterSectionExperimentService {
    int insert(List<ChapterSectionExperimentEntity> list);

    List<String> getExperimentIds(List<String> section_ids);

    Pair<Boolean, String> deleteChapterSectionExperiment(String section_id, String experiment_id);

    int getExperimentNumberbySection(String section_id);

    int getSectionNumberbyExperiment(String experiment_id);

}
