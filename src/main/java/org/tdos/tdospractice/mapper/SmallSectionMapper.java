package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.type.Section;
import org.tdos.tdospractice.type.SmallSection;

@Mapper
@Repository
public interface SmallSectionMapper {

    void modifySmallSectionNameById(@Param("id") String id, @Param("sectionSmallName") String sectionSmallName);

    int hasSmallSection(String id);

    int insertSmallSection(SmallSection smallSection);

    int removeSmallSection(@Param("smallSectionId") String smallSectionId);
}
