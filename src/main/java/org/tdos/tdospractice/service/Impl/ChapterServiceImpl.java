package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.tdos.tdospractice.body.DeleteChapter;
import org.tdos.tdospractice.body.InsertChapter;
import org.tdos.tdospractice.entity.CourseChapterSectionEntity;
import org.tdos.tdospractice.mapper.ChapterMapper;
import org.tdos.tdospractice.mapper.CourseChapterSectionMapper;
import org.tdos.tdospractice.mapper.CourseMapper;
import org.tdos.tdospractice.mapper.SectionMapper;
import org.tdos.tdospractice.service.ChapterService;
import org.tdos.tdospractice.type.Chapter;
import org.tdos.tdospractice.type.Section;
import org.tdos.tdospractice.utils.UUIDPattern;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseChapterSectionMapper courseChapterSectionMapper;

    @Autowired
    private SectionMapper sectionMapper;

    private static final String EMPTY_UUID = "fb0a1080-b11e-427c-8567-56ca6105ea07";

    @Override
    public Pair<Boolean, String> modifyChapterNameById(String id, String chapterName, String introduction) {
        if (chapterMapper.hasChapter(id) > 0) {
            chapterMapper.modifyChapterNameById(id, chapterName, introduction);
            return new Pair<>(true, "");
        } else {
            return new Pair<>(false, "chapter id is not exist");
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Error.class})
    public Pair<Boolean, String> addChapter(InsertChapter insertChapter) {
        if (courseMapper.hasCourseExist(insertChapter.courseId) == 0) {
            return new Pair<>(false, "course is not exist");
        }
        Integer order = courseMapper.findCourseChapterOrder(insertChapter.courseId);
        Chapter chapter = new Chapter();
        chapter.name = insertChapter.chapterName;
        if (order == null){
            chapter.order = 0;
        }else{
            chapter.order = order + 1;
        }
        chapterMapper.insertChapter(chapter);
        courseChapterSectionMapper.insertCourseChapterSection(CourseChapterSectionEntity.builder()
                .courseId(insertChapter.courseId)
                .chapterId(chapter.id)
                .sectionId(EMPTY_UUID)
                .smallSectionId(EMPTY_UUID)
                .build());
        return new Pair<>(true, "");
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Error.class})
    public Pair<Boolean, String> removeChapter(DeleteChapter deleteChapter) {
        if (!UUIDPattern.isValidUUID(deleteChapter.chapterId)){
            return new Pair<>(false, "chapter_id is not be uuid");
        }
        if (ObjectUtils.isEmpty(deleteChapter.chapterId)){
            return new Pair<>(false, "chapter is not exist");
        }
        if (chapterMapper.hasChapter(deleteChapter.chapterId) == 0) {
            return new Pair<>(false, "chapter is not exist");
        }
        Chapter chapter = chapterMapper.getChapter(deleteChapter.chapterId);
        chapter.sections.forEach(x-> sectionMapper.removeSection(x.id));
        chapterMapper.removeChapter(deleteChapter.chapterId);
        return new Pair<>(true, "");
    }

}
