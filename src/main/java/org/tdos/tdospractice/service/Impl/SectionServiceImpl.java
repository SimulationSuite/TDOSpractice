package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.tdos.tdospractice.body.DeleteSection;
import org.tdos.tdospractice.body.InsertSection;
import org.tdos.tdospractice.entity.CourseChapterSectionEntity;
import org.tdos.tdospractice.mapper.*;
import org.tdos.tdospractice.service.SectionService;
import org.tdos.tdospractice.type.Section;
import org.tdos.tdospractice.type.SmallSection;
import org.tdos.tdospractice.utils.UUIDPattern;

import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private SmallSectionMapper smallSectionMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseChapterSectionMapper courseChapterSectionMapper;

    private static final String EMPTY_UUID = "fb0a1080-b11e-427c-8567-56ca6105ea07";

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
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
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
                .smallSectionId(EMPTY_UUID)
                .build());
        return new Pair<>(true, "");
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Pair<Boolean, String> removeSection(DeleteSection deleteSection) {
        if (ObjectUtils.isEmpty(deleteSection.sectionId)) {
            return new Pair<>(false, "section is not exist");
        }
        if (!UUIDPattern.isValidUUID(deleteSection.sectionId)) {
            return new Pair<>(false, "section_id is not be uuid");
        }
        if (sectionMapper.hasSection(deleteSection.sectionId) == 0) {
            return new Pair<>(false, "section is not exist");
        }
        Section section = sectionMapper.getSection(deleteSection.sectionId);
        section.smallSections.stream().filter(smallSection -> !smallSection.id.equals(EMPTY_UUID)).forEach(x -> smallSectionMapper.removeSmallSection(x.id));
        sectionMapper.removeSection(deleteSection.sectionId);
        return new Pair<>(true, "");
    }

}
