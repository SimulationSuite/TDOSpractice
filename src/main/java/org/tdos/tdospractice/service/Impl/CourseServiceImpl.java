package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.PrepareCourse;
import org.tdos.tdospractice.entity.ClassCourse;
import org.tdos.tdospractice.entity.CourseChapterSectionEntity;
import org.tdos.tdospractice.mapper.*;
import org.tdos.tdospractice.service.CourseService;
import org.tdos.tdospractice.type.Chapter;
import org.tdos.tdospractice.type.Course;
import org.tdos.tdospractice.type.Section;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ClassCourseMapper classCourseMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private CourseChapterSectionMapper courseChapterSectionMapper;

    @Override
    public List<Course> getAdminCourseList() {
        List<Course> courses = courseMapper.getAdminCourseList();
        courses.forEach(x -> {
            x.chapters.forEach(s -> {
                s.sections = s.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList());
            });
            x.chapters = x.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
        });
        return courses;
    }

    @Override
    public List<Course> getAdminCourseListByClassId(String classId) {
        List<String> courseIds = classCourseMapper
                .findListByClassId(classId).stream().map(ClassCourse::getCourseId).collect(Collectors.toList());
        return courseMapper.getAdminCourseList().stream().filter(x -> courseIds.contains(x.id)).collect(Collectors.toList());
    }

    @Override
    public Pair<Boolean, String> prepareCourse(PrepareCourse prepareCourse) {
        if (courseMapper.hasCourseExist(prepareCourse.courseId) == 0) {
            return new Pair<>(false, "course is not exist");
        }
        Course course = courseMapper.getAdminCourseByCourseId(prepareCourse.courseId);
        course.ownerId = prepareCourse.user_id;
        course.type = 1;
        course.modelId = course.id;
        courseMapper.insertPrepareCourse(course);
        List<CourseChapterSectionEntity> list = new ArrayList<>();
        course.chapters.forEach(x -> {
            chapterMapper.insertPrepareChapter(x);
            x.sections.forEach(section -> {
                sectionMapper.insertPrepareSection(section);
                list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                        .chapterId(x.id)
                        .sectionId(section.id)
                        .build());
            });
        });
        courseChapterSectionMapper.insertCourseChapterSection(list);
        return new Pair<>(true, "");
    }
}
