package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.tdos.tdospractice.body.Assignment;
import org.tdos.tdospractice.entity.AssignmentEntity;
import org.tdos.tdospractice.entity.CoursewareEntity;

import java.util.List;
import java.util.Map;

public interface AssignmentService {

    PageInfo<AssignmentEntity> getAssignmentByClassId(String classId, Integer perPage,Integer page);

    PageInfo<AssignmentEntity> getAssignmentByCourseId(String courseId, Integer perPage,Integer page);

    PageInfo<AssignmentEntity> getAssignmentByChapterId(String chapterId, Integer perPage,Integer page);

    PageInfo<AssignmentEntity> getAssignmentBySectionId(String sectionId, Integer perPage,Integer page);

    Map<String, Object> deleteAssignmentById(List<String> id);

    AssignmentEntity addAssignment(Assignment assignment);

    Boolean modifyAssignmentNameById(Assignment assignment);

}
