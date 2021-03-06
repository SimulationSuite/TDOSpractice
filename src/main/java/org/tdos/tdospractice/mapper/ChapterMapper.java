package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.type.Chapter;

@Mapper
@Repository
public interface ChapterMapper {

    void modifyChapterNameById(@Param("id") String id, @Param("chapterName") String chapterName,@Param("introduction") String introduction);

    int hasChapter(String id);

    int insertChapter(Chapter chapter);

    Chapter getChapter(String chapterId);

    int removeChapter(String chapterId);

    Chapter getSlightChapter(String chapterId);
}
