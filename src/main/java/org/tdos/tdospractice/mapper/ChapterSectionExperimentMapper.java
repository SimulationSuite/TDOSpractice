package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ChapterSectionExperimentEntity;

import java.util.List;

@Mapper
@Repository
public interface ChapterSectionExperimentMapper {

    int insert(@Param("list") List<ChapterSectionExperimentEntity> list);

    List<String> getExperimentIds(@Param("section_ids")List<String> section_ids);

    boolean deleteChapterSectionExperiment(String id);

    int getExperimentNumberbySection(String section_id);

    int getSectionNumberbyExperiment(String experiment_id);

}
