package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.Assignment;
import org.tdos.tdospractice.body.StudentAnswer;
import org.tdos.tdospractice.entity.StudentAnswerEntity;
import org.tdos.tdospractice.mapper.StudentAnswerMapper;
import org.tdos.tdospractice.service.StudentAnswerService;

import java.util.*;

@Service
public class StudentAnswerServiceImpl implements StudentAnswerService {

    @Autowired
    private StudentAnswerMapper studentAnswerMapper;

    @Override
    public List<StudentAnswerEntity> getStudentAnswerBySectionId(String sectionId) {
        return studentAnswerMapper.getStudentAnswerBySectionId(sectionId);
    }

    @Override
    public Map<String, Object> deleteStudentAnswerById(List<String> id) {
        Map<String, Object> map = new HashMap<>();
        List<String> sectionStudentAnswer = new ArrayList<>();
        id.forEach(x -> {
            if (!studentAnswerMapper.ifSectionStudentAnswerById(x)){
                sectionStudentAnswer.add(x);
            }
        });
        if (sectionStudentAnswer.size() > 0){
            map.put("isDelete", false);
            map.put("notDeleteId", sectionStudentAnswer);
            return map;
        }
        id.forEach(x -> studentAnswerMapper.deleteStudentAnswerById(x));
        map.put("isDelete", true);
        map.put("notDeleteId", sectionStudentAnswer);
        return map;
    }

    @Override
    public StudentAnswerEntity addStudentAnswer(StudentAnswer studentAnswer) {
        StudentAnswerEntity studentAnswerEntity = new StudentAnswerEntity();
        studentAnswerEntity.setQuestionId(studentAnswer.questionId);
        studentAnswerEntity.setAssignmentId(studentAnswer.assignmentId);
        studentAnswerEntity.setUserId(studentAnswer.userId);
        studentAnswerEntity.setAnswer(studentAnswer.answer);
        studentAnswerEntity.setScore(studentAnswer.score);
        try {
            studentAnswerMapper.addStudentAnswer(studentAnswerEntity);
        } catch (Exception e) {
            return studentAnswerEntity;
        }
        return studentAnswerEntity;
    }

    @Override
    public Boolean modifyStudentAnswerById(StudentAnswer studentAnswer) {
        try {
            StudentAnswerEntity studentAnswerEntity = new StudentAnswerEntity();
            studentAnswerEntity.setId(studentAnswer.id);
            studentAnswerEntity.setQuestionId(studentAnswer.questionId);
            studentAnswerEntity.setAssignmentId(studentAnswer.assignmentId);
            studentAnswerEntity.setUserId(studentAnswer.userId);
            studentAnswerEntity.setAnswer(studentAnswer.answer);
            studentAnswerEntity.setScore(studentAnswer.score);
            studentAnswerMapper.modifyStudentAnswerById(studentAnswerEntity);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}