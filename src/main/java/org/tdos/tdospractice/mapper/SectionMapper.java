package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SectionMapper {

    void modifySectionNameById(@Param("id") String id, @Param("sectionName") String sectionName);

    int hasSection(String id);

}
