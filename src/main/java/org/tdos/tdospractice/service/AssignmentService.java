package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.tdos.tdospractice.body.Assignment;
import org.tdos.tdospractice.entity.AssignmentEntity;
import org.tdos.tdospractice.entity.CoursewareEntity;

import java.util.List;
import java.util.Map;

public interface AssignmentService {

    List<AssignmentEntity> getAssignmentByClassId(String classId);

    List<AssignmentEntity> getAssignmentByCourseId(String courseId);

    List<AssignmentEntity> getAssignmentByChapterId(String chapterId);

    List<AssignmentEntity> getAssignmentBySectionId(String sectionId);

    Map<String, Object> deleteAssignmentById(List<String> id);

    AssignmentEntity addAssignment(Assignment assignment);

    Boolean modifyAssignmentNameById(Assignment assignment);

}
