package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.StudentScore;
import org.tdos.tdospractice.entity.StudentScoreEntity;
import org.tdos.tdospractice.mapper.StudentScoreMapper;
import org.tdos.tdospractice.service.StudentScoreService;

import java.util.*;

@Service
public class StudentScoreServiceImpl implements StudentScoreService {

    @Autowired
    private StudentScoreMapper studentScoreMapper;

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
    public StudentScoreEntity addStudentScore(StudentScore studentScore) {
        StudentScoreEntity studentScoreEntity = new StudentScoreEntity();
        studentScoreEntity.setAssignmentId(studentScore.assignmentId);
        studentScoreEntity.setUserId(studentScore.userId);
        studentScoreEntity.setScore(studentScore.score);
        studentScoreEntity.setStatus(studentScore.status);
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
            studentScoreEntity.setId(studentScore.id);
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

}
