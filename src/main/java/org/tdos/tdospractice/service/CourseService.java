package org.tdos.tdospractice.service;


import org.tdos.tdospractice.type.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAdminCourseList();

    List<Course> getAdminCourseListByClassId(String classId);

}
