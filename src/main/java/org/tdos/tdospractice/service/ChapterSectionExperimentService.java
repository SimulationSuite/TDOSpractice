package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.ChapterSectionExperimentEntity;

import java.util.List;

public interface ChapterSectionExperimentService {
    int insert(List<ChapterSectionExperimentEntity> list);

    List<String> getExperimentIds(List<String> section_ids);
}
