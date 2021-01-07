package org.tdos.tdospractice.service;


import javafx.util.Pair;
import org.tdos.tdospractice.body.PrepareCourse;
import org.tdos.tdospractice.type.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAdminCourseList();

    List<Course> getAdminCourseListByClassId(String classId);

    Pair<Boolean, String> prepareCourse(PrepareCourse prepareCourse);
}
