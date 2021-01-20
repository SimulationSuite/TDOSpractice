package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.QuestionBack;;
import org.tdos.tdospractice.entity.QuestionBackEntity;
import org.tdos.tdospractice.body.QuestionBackAssignment;;
import org.tdos.tdospractice.entity.QuestionBackAssignmentEntity;
import org.tdos.tdospractice.mapper.QuestionBackMapper;
import org.tdos.tdospractice.service.QuestionBackService;

import java.util.*;

@Service
public class QuestionBackServiceImpl implements QuestionBackService {

    @Autowired
    private QuestionBackMapper questionBackMapper;

    @Override
    public Map<String, Object> deleteQuestionBackById(List<String> id) {
        Map<String, Object> map = new HashMap<>();
        List<String> sectionQuestionBack = new ArrayList<>();
        id.forEach(x -> {
            if (!questionBackMapper.ifSectionQuestionBackByQuestionBackId(x)){
                sectionQuestionBack.add(x);
            }
        });
        if (sectionQuestionBack.size() > 0){
            map.put("isDelete", false);
            map.put("notDeleteId", sectionQuestionBack);
            return map;
        }
        id.forEach(x -> questionBackMapper.deleteQuestionBackById(x));
        map.put("isDelete", true);
        map.put("notDeleteId", sectionQuestionBack);
        return map;
    }

    @Override
    public QuestionBackEntity addQuestionBack(QuestionBack questionBack) {
        QuestionBackEntity questionBackEntity = new QuestionBackEntity();
        questionBackEntity.setType(questionBack.type);
        questionBackEntity.setAnswer(questionBack.answer);
        questionBackEntity.setQuestion(questionBack.question);
        questionBackEntity.setModelId(questionBack.modelId);
        questionBackEntity.setCategoryId(questionBack.categoryId);
        try {
            questionBackMapper.addQuestionBack(questionBackEntity);
        } catch (Exception e) {
            return questionBackEntity;
        }
        return questionBackEntity;
    }

    @Override
    public Boolean modifyQuestionBackById(QuestionBack questionBack) {
        try {
            QuestionBackEntity questionBackEntity = new QuestionBackEntity();
            questionBackEntity.setId(questionBack.id);
            questionBackEntity.setType(questionBack.type);
            questionBackEntity.setAnswer(questionBack.answer);
            questionBackEntity.setQuestion(questionBack.question);
            questionBackEntity.setModelId(questionBack.modelId);
            questionBackEntity.setCategoryId(questionBack.categoryId);
            questionBackMapper.modifyQuestionBackNameById(questionBackEntity);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public QuestionBackEntity getStudentAnswerByAssignment(String assignmentId) {
        try {
            return questionBackMapper.getStudentAnswerByAssignment(assignmentId);
        } catch (Exception e) {
            return new QuestionBackEntity();
        }
    }

    @Override
    public QuestionBackAssignmentEntity addQuestionBackAssignment(QuestionBackAssignment questionBackAssignment) {
        QuestionBackAssignmentEntity questionBackAssignmentEntity = new QuestionBackAssignmentEntity();
        questionBackAssignmentEntity.setAssignmentId(questionBackAssignment.assignmentId);
        questionBackAssignmentEntity.setQuestionId(questionBackAssignment.questionId);
        questionBackAssignmentEntity.setScore(questionBackAssignment.score);
        try {
            questionBackMapper.addQuestionBackAssignment(questionBackAssignmentEntity);
        } catch (Exception e) {
            return questionBackAssignmentEntity;
        }
        return questionBackAssignmentEntity;
    }

    @Override
    public List<QuestionBackAssignmentEntity> addQuestionBackAssignmentList(List<QuestionBackAssignment> questionBackAssignmentList) {
        List<QuestionBackAssignmentEntity> questionBackAssignmentEntityList = new ArrayList<QuestionBackAssignmentEntity>();
        questionBackAssignmentList.forEach(x -> {
            QuestionBackAssignmentEntity questionBackAssignmentEntity = new QuestionBackAssignmentEntity();
            questionBackAssignmentEntity.setAssignmentId(x.assignmentId);
            questionBackAssignmentEntity.setQuestionId(x.questionId);
            questionBackAssignmentEntity.setScore(x.score);
            questionBackAssignmentEntityList.add(questionBackAssignmentEntity);
        });
        try {
            questionBackMapper.addQuestionBackAssignmentList(questionBackAssignmentEntityList);
        } catch (Exception e) {
            return questionBackAssignmentEntityList;
        }
        return questionBackAssignmentEntityList;
    }

}
