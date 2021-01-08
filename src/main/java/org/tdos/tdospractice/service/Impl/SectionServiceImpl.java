package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.InsertSection;
import org.tdos.tdospractice.entity.CourseChapterSectionEntity;
import org.tdos.tdospractice.mapper.ChapterMapper;
import org.tdos.tdospractice.mapper.CourseChapterSectionMapper;
import org.tdos.tdospractice.mapper.CourseMapper;
import org.tdos.tdospractice.mapper.SectionMapper;
import org.tdos.tdospractice.service.SectionService;
import org.tdos.tdospractice.type.Section;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseChapterSectionMapper courseChapterSectionMapper;

    @Override
    public Pair<Boolean, String> modifySectionNameById(String id, String sectionName) {
        if (sectionMapper.hasSection(id) > 0) {
            sectionMapper.modifySectionNameById(id, sectionName);
            return new Pair<>(true, "");
        } else {
            return new Pair<>(false, "chapter id is not exist");
        }
    }

    @Override
    public Pair<Boolean, String> addSection(InsertSection insertSection) {
        if (courseMapper.hasCourseExist(insertSection.courseId) == 0) {
            return new Pair<>(false, "course is not exist");
        }
        if (chapterMapper.hasChapter(insertSection.chapterId) == 0) {
            return new Pair<>(false, "chapter is not exist");
        }
        Integer order = courseMapper.findCourseChapterSectionOrder(insertSection.courseId, insertSection.chapterId);
        Section section = new Section();
        section.name = insertSection.sectionName;
        if (order == null) {
            section.order = 0;
        } else {
            section.order = order + 1;
        }
        sectionMapper.insertSection(section);
        courseChapterSectionMapper.insertCourseChapterSection(CourseChapterSectionEntity.builder()
                .courseId(insertSection.courseId)
                .chapterId(insertSection.chapterId)
                .sectionId(section.id)
                .build());
        return new Pair<>(true, "");
    }

}
