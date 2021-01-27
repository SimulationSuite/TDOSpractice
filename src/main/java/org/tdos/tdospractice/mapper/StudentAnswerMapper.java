package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.QuestionBackEntity;
import org.tdos.tdospractice.entity.StudentAnswerEntity;

import java.util.List;

@Mapper
@Repository
public interface StudentAnswerMapper {

    List<QuestionBackEntity> getStudentAnswerByAssignmentUserId(@Param("userId") String userId, @Param("assignmentId") String assignmentId);

    List<StudentAnswerEntity> getStudentAnswerByCourseId(@Param("courseId") String courseId);

    List<StudentAnswerEntity> getStudentAnswerByChapterId(@Param("chapterId") String chapterId);

    List<StudentAnswerEntity> getStudentAnswerBySectionId(@Param("sectionId") String sectionId);

    int deleteStudentAnswerById(@Param("id") String id);

    boolean ifSectionStudentAnswerById(String id);

    int modifyStudentAnswerById(StudentAnswerEntity studentAnswer);

    int addStudentAnswer(StudentAnswerEntity studentAnswer);

    int addStudentAnswerList(@Param("studentAnswerList") List<StudentAnswerEntity> studentAnswerList);

    int modifyStudentAnswerScore(@Param("score") int score, @Param("questionId") String questionId,@Param("assignmentId") String assignmentId,@Param("userId") String userId);

}
