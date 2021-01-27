package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.StudentScore;
import org.tdos.tdospractice.entity.StudentAnswerEntity;
import org.tdos.tdospractice.entity.StudentScoreEntity;
import org.tdos.tdospractice.mapper.StudentAnswerMapper;
import org.tdos.tdospractice.mapper.StudentScoreMapper;
import org.tdos.tdospractice.service.StudentScoreService;

import java.util.*;

@Service
public class StudentScoreServiceImpl implements StudentScoreService {

    @Autowired
    private StudentScoreMapper studentScoreMapper;

    @Autowired
    private StudentAnswerMapper studentAnswerMapper;

    @Override
    public List<StudentScoreEntity> getStudentScoreBySectionId(String sectionId) {
        return studentScoreMapper.getStudentScoreBySectionId(sectionId);
    }

    @Override
    public Map<String, Object> deleteStudentScoreById(List<String> id) {
        Map<String, Object> map = new HashMap<>();
        List<String> sectionStudentScore = new ArrayList<>();
        id.forEach(x -> {
            if (!studentScoreMapper.ifSectionStudentScoreById(x)){
                sectionStudentScore.add(x);
            }
        });
        if (sectionStudentScore.size() > 0){
            map.put("isDelete", false);
            map.put("notDeleteId", sectionStudentScore);
            return map;
        }
        id.forEach(x -> studentScoreMapper.deleteStudentScoreById(x));
        map.put("isDelete", true);
        map.put("notDeleteId", sectionStudentScore);
        return map;
    }

    @Override
    public StudentScoreEntity addStudentScore(List<StudentScore> studentScore) {
        int total = studentScore.stream().mapToInt(x->x.score).sum();
        studentScore.forEach( x -> {
            studentAnswerMapper.modifyStudentAnswerScore(x.score, x.questionId, x.assignmentId, x.userId);
        });
        StudentScoreEntity studentScoreEntity = new StudentScoreEntity();
        studentScoreEntity.setAssignmentId(studentScore.get(0).assignmentId);
        studentScoreEntity.setUserId(studentScore.get(0).userId);
        studentScoreEntity.setScore(total);
        studentScoreEntity.setStatus(1);
        try {
            studentScoreMapper.addStudentScore(studentScoreEntity);
        } catch (Exception e) {
            return studentScoreEntity;
        }
        return studentScoreEntity;
    }

    @Override
    public Boolean modifyStudentScoreById(StudentScore studentScore) {
        try {
            StudentScoreEntity studentScoreEntity = new StudentScoreEntity();
            studentScoreEntity.setAssignmentId(studentScore.assignmentId);
            studentScoreEntity.setUserId(studentScore.userId);
            studentScoreEntity.setScore(studentScore.score);
            studentScoreEntity.setStatus(studentScore.status);
            studentScoreMapper.modifyStudentScoreById(studentScoreEntity);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<StudentScoreEntity> getStudentScoreAll() {
        return studentScoreMapper.getStudentScoreAll();
    }

}
