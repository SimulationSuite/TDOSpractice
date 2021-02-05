package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.tdos.tdospractice.body.Assignment;
import org.tdos.tdospractice.entity.AssignmentEntity;
import org.tdos.tdospractice.type.AssignmentQuestionBack;
import org.tdos.tdospractice.type.AssignmentStatistics;
import org.tdos.tdospractice.type.StudentAssignment;

import java.util.List;
import java.util.Map;

public interface AssignmentService {

    PageInfo<StudentAssignment> getStudentAssignment(String userId, String courseId,String chapterId, String sectionId, Integer status,String name, Integer perPage, Integer page);

    AssignmentStatistics getAssignmentStatisticsBySectionId(String sectionId);

    PageInfo<StudentAssignment> getAssignmentAll(String classId,String courseId,String chapterId, String sectionId, Integer status,String name,String startTime,String endTime, Integer perPage, Integer page);

    PageInfo<AssignmentEntity> getAssignmentByClassId(String classId, Integer perPage,Integer page);

    PageInfo<AssignmentEntity> getAssignmentByCourseId(String courseId, Integer perPage,Integer page);

    PageInfo<AssignmentEntity> getAssignmentByChapterId(String chapterId, Integer perPage,Integer page);

    PageInfo<AssignmentQuestionBack> getAssignmentBySectionId(String sectionId, Integer perPage, Integer page);

    AssignmentEntity getAssignmentNameBySectionId(String sectionId);

    Map<String, Object> deleteAssignmentById(List<String> id);

    Pair<Boolean, Object> addAssignment(Assignment assignment);

    Boolean modifyAssignmentNameById(Assignment assignment);

    Boolean modifyAssignmentStatusById(Assignment assignment);

    void updateEndAssignment(String nowTime);

}
