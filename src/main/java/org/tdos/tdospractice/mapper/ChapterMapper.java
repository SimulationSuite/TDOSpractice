package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.CourseChapterSectionEntity;
import org.tdos.tdospractice.type.Chapter;
import org.tdos.tdospractice.type.Course;

@Mapper
@Repository
public interface ChapterMapper {

    void modifyChapterNameById(@Param("id") String id, @Param("chapterName") String chapterName,@Param("introduction") String introduction);

    int hasChapter(String id);

    int insertChapter(Chapter chapter);
}
