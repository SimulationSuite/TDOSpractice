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

    List<StudentAssignment> getStudentAssignment(@Param("userId") String userId, @Param("courseId") String courseId, @Param("chapterId") String chapterId, @Param("sectionId") String sectionId, @Param("name") String name, @Param("status") String status);

    List<StudentAnswerEntity> getAllStudentAssignmentBySectionId(@Param("sectionId") String sectionId);

    List<StudentAnswerEntity> getSubStudentAssignmentBySectionId(@Param("sectionId") String sectionId);

    List<StudentAssignment> getAssignmentAll(@Param("classId") String classId, @Param("courseId") String courseId, @Param("chapterId") String chapterId, @Param("sectionId") String sectionId, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("ownerId") String ownerId, @Param("status") String status);

    List<AssignmentEntity> getAssignmentByClassId(@Param("classId") String classId);

    List<AssignmentEntity> getAssignmentByCourseId(@Param("courseId") String courseId);

    List<AssignmentEntity> getAssignmentByChapterId(@Param("chapterId") String chapterId);

    List<AssignmentQuestionBack> getAssignmentBySectionId(@Param("sectionId") String sectionId, @Param("type") Integer type);

    AssignmentEntity getAssignmentNameBySectionId(@Param("sectionId") String sectionId);

    int deleteAssignmentById(@Param("id") String id);

    int ifSectionAssignmentByAssignmentId(String id);

    boolean ifAssignmentBySectionId(@Param("sectionId") String sectionId);

    int modifyAssignmentNameById(@Param("id") String id, @Param("name") String name, @Param("endAt") String endAt);

    int addAssignment(AssignmentEntity assignment);

    int addAssignmentList(@Param("assignmentList") List<AssignmentEntity> assignmentList);

    int modifyAssignmentStatusById(@Param("id") String id, @Param("status") Integer status);

    List<AssignmentEntity> getEndAssignment(@Param("endTime") String endTime);

    List<String> getUsers(@Param("assignmentId") String assignmentId);

    int ifStudentAnswer(@Param("userId") String userId, @Param("assignmentId") String assignmentId);

    int studentAnswerStatus(@Param("userId") String userId, @Param("assignmentId") String assignmentId);

    List<StudentAnswerEntity> getQuestionBackByAssignment(@Param("assignmentId") String assignmentId);

    int hasAssignmentNameExist(@Param("assignmentId") String assignmentId, @Param("name") String name);

    int deleteQuestionBackAssignmentById(@Param("id") String id);

}
