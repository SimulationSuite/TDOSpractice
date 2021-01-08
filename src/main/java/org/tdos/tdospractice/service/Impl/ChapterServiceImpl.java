package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.InsertChapter;
import org.tdos.tdospractice.entity.CourseChapterSectionEntity;
import org.tdos.tdospractice.mapper.ChapterMapper;
import org.tdos.tdospractice.mapper.CourseChapterSectionMapper;
import org.tdos.tdospractice.mapper.CourseMapper;
import org.tdos.tdospractice.service.ChapterService;
import org.tdos.tdospractice.type.Chapter;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseChapterSectionMapper courseChapterSectionMapper;

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
    public Pair<Boolean, String> addChapter(InsertChapter insertChapter) {
        if (courseMapper.hasCourseExist(insertChapter.courseId) == 0) {
            return new Pair<>(false, "course is not exist");
        }
        Integer order = courseMapper.findCourseChapterOrder(insertChapter.courseId);
        Chapter chapter = new Chapter();
        chapter.introduction = insertChapter.introduction;
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
                .build());
        return new Pair<>(true, "");
    }

}
