package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.StudentAnswer;
import org.tdos.tdospractice.body.StudentScore;
import org.tdos.tdospractice.entity.StudentAnswerEntity;
import org.tdos.tdospractice.entity.StudentScoreEntity;
import org.tdos.tdospractice.mapper.StudentAnswerMapper;
import org.tdos.tdospractice.mapper.StudentScoreMapper;
import org.tdos.tdospractice.service.StudentAnswerService;
import org.tdos.tdospractice.type.StudentQuestionAnswer;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentAnswerServiceImpl implements StudentAnswerService {

    @Autowired
    private StudentAnswerMapper studentAnswerMapper;

    @Autowired
    private StudentScoreMapper studentScoreMapper;

    @Override
    public PageInfo<StudentQuestionAnswer> getStudentAnswerByAssignmentUserId(String userId, String assignmentId, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<StudentQuestionAnswer> list = studentAnswerMapper.getStudentAnswerByAssignmentUserId(userId, assignmentId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<StudentAnswerEntity> getStudentAnswerByCourseId(String sectionId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<StudentAnswerEntity> list = studentAnswerMapper.getStudentAnswerBySectionId(sectionId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<StudentAnswerEntity> getStudentAnswerByChapterId(String sectionId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<StudentAnswerEntity> list = studentAnswerMapper.getStudentAnswerBySectionId(sectionId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<StudentAnswerEntity> getStudentAnswerBySectionId(String sectionId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<StudentAnswerEntity> list = studentAnswerMapper.getStudentAnswerBySectionId(sectionId);
        return new PageInfo<>(list);
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

    @Override
    public List<StudentAnswerEntity> addStudentAnswerList(List<StudentAnswer> studentAnswerList) {
        List<StudentAnswerEntity> studentAnswerEntityList = new ArrayList<StudentAnswerEntity>();
        studentAnswerList.forEach(x -> {
            StudentAnswerEntity studentAnswerEntity = new StudentAnswerEntity();
            studentAnswerEntity.setQuestionId(x.questionId);
            studentAnswerEntity.setAssignmentId(x.assignmentId);
            studentAnswerEntity.setUserId(x.userId);
            studentAnswerEntity.setAnswer(x.answer);
            studentAnswerEntity.setScore(x.score);
            studentAnswerEntityList.add(studentAnswerEntity);
        });

        try {
            studentAnswerMapper.addStudentAnswerList(studentAnswerEntityList);
        } catch (Exception e) {
            System.out.println(e);
            return studentAnswerEntityList;
        }
        return studentAnswerEntityList;
    }

    @Override
    public Boolean modifyStudentAnswerStatusById(StudentAnswer studentAnswer) {
        try {
            String assignmentId = studentAnswer.assignmentId;
            String userId = studentAnswer.userId;
            List<StudentQuestionAnswer> list = studentAnswerMapper.getQuestionBackTypeByAssignment(assignmentId);
            if(list.size() == 1 && list.get(0).getType() == 0){
                List<StudentQuestionAnswer> studentQuestionAnswerList = studentAnswerMapper.getStudentAnswerByAssignmentUserId(assignmentId, userId);
                List<StudentQuestionAnswer> rightStudentQuestionAnswerList = studentQuestionAnswerList.stream().filter(x -> x.getAnswer() == x.getStudentAnswer()).collect(Collectors.toList());
                int score = rightStudentQuestionAnswerList.stream().mapToInt(x->x.getScore()).sum();
                StudentScoreEntity studentScoreEntity = new StudentScoreEntity();
                studentScoreEntity.setAssignmentId(assignmentId);
                studentScoreEntity.setUserId(userId);
                studentScoreEntity.setStatus(1);
                studentScoreEntity.setStatus(score);
                studentScoreMapper.addStudentScore(studentScoreEntity);
            }
            studentAnswerMapper.modifyStudentAnswerStatus(1, new Date(), assignmentId, userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
