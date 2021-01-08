package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.CourseChapterSectionEntity;

import java.util.List;

@Mapper
@Repository
public interface CourseChapterSectionMapper {

    int insertCourseChapterSectionList(List<CourseChapterSectionEntity> list);

    int insertCourseChapterSection(CourseChapterSectionEntity courseChapterSectionEntity);

}
