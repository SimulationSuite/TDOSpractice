package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.tdos.tdospractice.body.QuestionBack;
import org.tdos.tdospractice.body.QuestionBackAssignment;
import org.tdos.tdospractice.entity.QuestionBackEntity;
import org.tdos.tdospractice.entity.QuestionBackAssignmentEntity;
import org.tdos.tdospractice.type.StudentQuestionAnswer;

import java.util.List;
import java.util.Map;

public interface QuestionBackService {

    PageInfo<QuestionBackEntity> getQuestionBackAll(Integer type, String content, String categoryId, String cCategoryId, String assignmentId, Integer perPage, Integer page);

    Map<String, Object> deleteQuestionBackById(List<String> id);

    Pair<Boolean, Object> addQuestionBack(QuestionBack questionBack);

    Boolean modifyQuestionBackById(QuestionBack questionBack);

    PageInfo<StudentQuestionAnswer> getStudentAnswerByAssignment(String userId, String assignmentId, Integer perPage, Integer page);

    QuestionBackAssignmentEntity addQuestionBackAssignment(QuestionBackAssignment questionBackAssignment);

    List<QuestionBackAssignmentEntity> addQuestionBackAssignmentList(List<QuestionBackAssignment> questionBackAssignmentList);

}
