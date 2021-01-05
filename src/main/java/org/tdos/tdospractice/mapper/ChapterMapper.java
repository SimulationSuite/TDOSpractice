package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ChapterMapper {

    void modifyChapterNameById(@Param("id") String id, @Param("chapterName") String chapterName,@Param("introduction") String introduction);

    int hasChapter(String id);

}
