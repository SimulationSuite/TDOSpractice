package org.tdos.tdospractice.service;


import javafx.util.Pair;
import org.tdos.tdospractice.body.AddCourse;
import org.tdos.tdospractice.body.ModifyCourseStatus;
import org.tdos.tdospractice.body.PrepareCourse;
import org.tdos.tdospractice.type.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAdminCourseList();

    List<Course> getAdminCourseListByClassId(String classId);

    Pair<Boolean, String> prepareCourse(PrepareCourse prepareCourse);

    List<Course> getCourseListById(String userId);

    Course AddAdminCourse(AddCourse addCourse);

    Pair<Boolean, String> modifyCourseStatus(ModifyCourseStatus modifyCourseStatus);
}
