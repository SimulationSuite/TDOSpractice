package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import org.tdos.tdospractice.body.QuestionBack;
import org.tdos.tdospractice.body.QuestionBackAssignment;
import org.tdos.tdospractice.entity.QuestionBackEntity;
import org.tdos.tdospractice.entity.QuestionBackAssignmentEntity;

import java.util.List;
import java.util.Map;

public interface QuestionBackService {

    PageInfo<QuestionBackEntity> getQuestionBackAll(Integer type, String content, String categoryId, Integer perPage, Integer page);

    Map<String, Object> deleteQuestionBackById(List<String> id);

    QuestionBackEntity addQuestionBack(QuestionBack questionBack);

    Boolean modifyQuestionBackById(QuestionBack questionBack);

    QuestionBackEntity getStudentAnswerByAssignment(String assignmentId);

    QuestionBackAssignmentEntity addQuestionBackAssignment(QuestionBackAssignment questionBackAssignment);

    List<QuestionBackAssignmentEntity> addQuestionBackAssignmentList(List<QuestionBackAssignment> questionBackAssignmentList);

}
