package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.InsertSmallSection;
import org.tdos.tdospractice.entity.CourseChapterSectionEntity;
import org.tdos.tdospractice.mapper.*;
import org.tdos.tdospractice.service.SmallSectionService;
import org.tdos.tdospractice.type.SmallSection;

@Service
public class SmallSectionServiceImpl implements SmallSectionService {

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseMapper courseMapper;


    @Autowired
    private SmallSectionMapper smallSectionMapper;

    @Autowired
    private CourseChapterSectionMapper courseChapterSectionMapper;


    @Override
    public Pair<Boolean, String> modifySmallSectionNameById(String sectionID, String smallSectionName) {
        if (smallSectionMapper.hasSmallSection(sectionID) > 0) {
            smallSectionMapper.modifySmallSectionNameById(sectionID, smallSectionName);
            return new Pair<>(true, "");
        } else {
            return new Pair<>(false, "chapter id is not exist");
        }
    }

    @Override
    public Pair<Boolean, String> addSmallSection(InsertSmallSection insertSmallSection) {
        if (courseMapper.hasCourseExist(insertSmallSection.courseId) == 0) {
            return new Pair<>(false, "course is not exist");
        }
        if (chapterMapper.hasChapter(insertSmallSection.chapterId) == 0) {
            return new Pair<>(false, "chapter is not exist");
        }
        if (sectionMapper.hasSection(insertSmallSection.sectionId) == 0) {
            return new Pair<>(false, "section is not exist");
        }
        Integer order = courseMapper.findSmallSectionOrder(insertSmallSection.sectionId);
        SmallSection smallSection = new SmallSection();
        smallSection.name = insertSmallSection.smallSectionName;
        if (order == null) {
            smallSection.order = 0;
        } else {
            smallSection.order = order + 1;
        }
        smallSectionMapper.insertSmallSection(smallSection);
        courseChapterSectionMapper.insertCourseChapterSection(CourseChapterSectionEntity.builder()
                .courseId(insertSmallSection.courseId)
                .chapterId(insertSmallSection.chapterId)
                .sectionId(insertSmallSection.sectionId)
                .smallSectionId(smallSection.id)
                .build());
        return new Pair<>(true, "");
    }
}
