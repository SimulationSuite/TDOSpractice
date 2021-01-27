package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.AssignmentEntity;
import org.tdos.tdospractice.entity.StudentAnswerEntity;

import java.util.List;

@Mapper
@Repository
public interface AssignmentMapper {

    List<StudentAnswerEntity> getAssignmentAll(@Param("classId") String classId, @Param("courseId") String courseId, @Param("chapterId") String chapterId, @Param("sectionId") String sectionId, @Param("name") String name);

    List<AssignmentEntity> getAssignmentByClassId(@Param("classId") String classId);

    List<AssignmentEntity> getAssignmentByCourseId(@Param("courseId") String courseId);

    List<AssignmentEntity> getAssignmentByChapterId(@Param("chapterId") String chapterId);

    List<AssignmentEntity> getAssignmentBySectionId(@Param("sectionId") String sectionId);

    int deleteAssignmentById(@Param("id") String id);

    boolean ifSectionAssignmentByAssignmentId(String id);

    int modifyAssignmentNameById(@Param("id") String id, @Param("name") String name);

    int addAssignment(AssignmentEntity assignment);

}
