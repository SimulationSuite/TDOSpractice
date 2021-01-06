package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.ClassCourse;
import org.tdos.tdospractice.mapper.ClassCourseMapper;
import org.tdos.tdospractice.mapper.CourseMapper;
import org.tdos.tdospractice.service.CourseService;
import org.tdos.tdospractice.type.Chapter;
import org.tdos.tdospractice.type.Course;
import org.tdos.tdospractice.type.Section;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ClassCourseMapper classCourseMapper;

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
}
