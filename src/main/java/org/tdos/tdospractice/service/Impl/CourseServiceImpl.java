package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.tdos.tdospractice.mapper.CourseMapper;
import org.tdos.tdospractice.service.CourseService;
import org.tdos.tdospractice.type.Course;

import java.util.List;

public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> getAdminCourseList() {
        return courseMapper.getAdminCourseList();
    }
}
