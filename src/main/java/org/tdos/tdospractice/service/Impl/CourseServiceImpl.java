package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.AddCourse;
import org.tdos.tdospractice.body.ModifyCourseStatus;
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
        courses = courses.stream().filter(x->x.status == 1).sorted(Comparator.comparing(x->x.name)).collect(Collectors.toList());
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
        Course course = courseMapper.getCourseByCourseId(prepareCourse.courseId);
        if (course.type  == 1){
            return new Pair<>(false, "select course is not admin public");
        }
        course.ownerId = prepareCourse.user_id;
        course.type = 1;
        course.modelId = course.id;
        writeCourse(course);
        return new Pair<>(true, "");
    }

    @Override
    public List<Course> getCourseListById(String userId) {
        List<Course> courses = courseMapper.getCourseListById(userId);
        courses = courses.stream().sorted(Comparator.comparing(x->x.name)).collect(Collectors.toList());
        return courses;
    }

    @Override
    public Course AddAdminCourse(AddCourse addCourse) {
        Course course = new Course();
        course.name = addCourse.name;
        course.picUrl = addCourse.picUrl;
        course.ownerId = addCourse.ownerId;
        course.introduction = addCourse.introduction;
        course.chapters = addCourse.chapters.stream().map(x->{
            Chapter chapter = new Chapter();
            chapter.introduction = x.introduction;
            chapter.name = x.name;
            chapter.order = x.order;
            chapter.sections = x.sections.stream().map(y->{
               Section section = new Section();
               section.name = y.name;
               section.order = y.order;
               return section;
            }).collect(Collectors.toList());
            return chapter;
        }).collect(Collectors.toList());

        return writeCourse(course);
    }

    @Override
    public Pair<Boolean, String> modifyCourseStatus(ModifyCourseStatus modifyCourseStatus) {
        if (courseMapper.hasCourseExist(modifyCourseStatus.courseId) == 0) {
            return new Pair<>(false, "course is not exist");
        }
        Course course = courseMapper.getCourseByCourseId(modifyCourseStatus.courseId);
        if (!course.ownerId.equals(modifyCourseStatus.userId)){
            return new Pair<>(false, "course is not belong to userId: "+ modifyCourseStatus.userId);
        }
        courseMapper.modifyCourseStatus(modifyCourseStatus.courseId);
        return new Pair<>(true, "");
    }

    private Course writeCourse(Course course){
        courseMapper.insertCourse(course);
        List<CourseChapterSectionEntity> list = new ArrayList<>();
        course.chapters.forEach(x -> {
            chapterMapper.insertChapter(x);
            x.sections.forEach(section -> {
                sectionMapper.insertSection(section);
                list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                        .chapterId(x.id)
                        .sectionId(section.id)
                        .build());
            });
        });
        courseChapterSectionMapper.insertCourseChapterSectionList(list);
        course.chapters.forEach(s -> {
            s.sections = s.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList());
        });
        course.chapters = course.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
        return course;
    }

}
