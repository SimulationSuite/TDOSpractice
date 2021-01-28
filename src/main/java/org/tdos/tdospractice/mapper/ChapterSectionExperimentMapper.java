package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ChapterSectionExperimentEntity;

@Mapper
@Repository
public interface ChapterSectionExperimentMapper {
    int insert(ChapterSectionExperimentEntity chapterSectionExperimentEntity);
}
