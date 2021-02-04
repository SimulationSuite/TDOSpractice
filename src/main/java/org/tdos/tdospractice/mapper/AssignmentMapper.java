package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.AssignmentEntity;
import org.tdos.tdospractice.entity.StudentAnswerEntity;
import org.tdos.tdospractice.type.StudentAssignment;
import org.tdos.tdospractice.type.AssignmentQuestionBack;

import java.util.List;

@Mapper
@Repository
public interface AssignmentMapper {

    List<StudentAssignment> getStudentAssignment(@Param("userId") String userId, @Param("courseId") String courseId, @Param("chapterId") String chapterId, @Param("sectionId") String sectionId, @Param("name") String name);

    List<StudentAnswerEntity> getAllStudentAssignmentBySectionId(@Param("sectionId") String sectionId);

    List<StudentAnswerEntity> getSubStudentAssignmentBySectionId(@Param("sectionId") String sectionId);

    List<StudentAssignment> getAssignmentAll(@Param("classId") String classId, @Param("courseId") String courseId, @Param("chapterId") String chapterId, @Param("sectionId") String sectionId, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<AssignmentEntity> getAssignmentByClassId(@Param("classId") String classId);

    List<AssignmentEntity> getAssignmentByCourseId(@Param("courseId") String courseId);

    List<AssignmentEntity> getAssignmentByChapterId(@Param("chapterId") String chapterId);

    List<AssignmentQuestionBack> getAssignmentBySectionId(@Param("sectionId") String sectionId);

    AssignmentEntity getAssignmentNameBySectionId(@Param("sectionId") String sectionId);

    int deleteAssignmentById(@Param("id") String id);

    boolean ifSectionAssignmentByAssignmentId(String id);

    boolean ifAssignmentBySectionId(@Param("sectionId") String sectionId);

    int modifyAssignmentNameById(@Param("id") String id, @Param("name") String name);

    int addAssignment(AssignmentEntity assignment);

    int modifyAssignmentStatusById(@Param("id") String id, @Param("status") Integer status);

    List<AssignmentEntity> getEndAssignment(@Param("endTime") String endTime);

    List<String> getUncommittedUser(@Param("assignmentId") String assignmentId);

}
