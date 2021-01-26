package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.QuestionBackEntity;
import org.tdos.tdospractice.entity.QuestionBackAssignmentEntity;

import java.util.List;

@Mapper
@Repository
public interface QuestionBackMapper {

    int deleteQuestionBackById(@Param("id") String id);

    boolean ifSectionQuestionBackByQuestionBackId(String id);

    int modifyQuestionBackNameById(QuestionBackEntity questionBack);

    int addQuestionBack(QuestionBackEntity questionBack);

    List<QuestionBackEntity> getQuestionBackAll(@Param("type") Integer type, @Param("categoryId") String categoryId, @Param("content") String content);

    QuestionBackEntity getStudentAnswerByAssignment(String assignmentId);

    int addQuestionBackAssignment(QuestionBackAssignmentEntity questionBackAssignment);

    int addQuestionBackAssignmentList(@Param("questionBackAssignmentList") List<QuestionBackAssignmentEntity> questionBackAssignmentList);

}
