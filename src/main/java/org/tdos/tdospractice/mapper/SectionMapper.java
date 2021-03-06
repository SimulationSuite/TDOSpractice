package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.type.Chapter;
import org.tdos.tdospractice.type.Section;
import org.tdos.tdospractice.type.SmallSection;

import java.util.List;

@Mapper
@Repository
public interface SectionMapper {

    void modifySectionNameById(@Param("id") String id, @Param("sectionName") String sectionName);

    int hasSection(String id);

    int insertSection(Section section);

    Section getSection(String sectionId);

    int removeSection(String sectionId);
}
