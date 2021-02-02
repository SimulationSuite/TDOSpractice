package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.QuestionBack;
import org.tdos.tdospractice.entity.QuestionBackEntity;
import org.tdos.tdospractice.body.QuestionBackAssignment;
import org.tdos.tdospractice.entity.QuestionBackAssignmentEntity;
import org.tdos.tdospractice.mapper.QuestionBackMapper;
import org.tdos.tdospractice.service.QuestionBackService;
import org.tdos.tdospractice.type.StudentQuestionAnswer;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionBackServiceImpl implements QuestionBackService {

    @Autowired
    private QuestionBackMapper questionBackMapper;

    @Override
    public PageInfo<QuestionBackEntity> getQuestionBackAll(Integer type, String content, String categoryId, String assignmentId, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<QuestionBackEntity> list = questionBackMapper.getQuestionBackAll(type, categoryId, content, assignmentId);
        return new PageInfo<>(list);
    }

    @Override
    public Map<String, Object> deleteQuestionBackById(List<String> id) {
        Map<String, Object> map = new HashMap<>();
        List<String> sectionQuestionBack = new ArrayList<>();
        id.forEach(x -> {
            if (questionBackMapper.ifExistId(x)){
                sectionQuestionBack.add(x);
            }
        });
        if (sectionQuestionBack.size() > 0){
            map.put("isDelete", false);
            map.put("notDeleteId", sectionQuestionBack);
            map.put("reason", "题目关联课程为已发布课程。");
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
        questionBackEntity.setContent(questionBack.content);
        questionBackEntity.setChoice(questionBack.choice);
        questionBackEntity.setAnswer(questionBack.answer);
        questionBackEntity.setPicUrl(questionBack.picUrl);
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
            questionBackEntity.setContent(questionBack.content);
            questionBackEntity.setChoice(questionBack.choice);
            questionBackEntity.setAnswer(questionBack.answer);
            questionBackEntity.setPicUrl(questionBack.picUrl);
            questionBackEntity.setModelId(questionBack.modelId);
            questionBackEntity.setCategoryId(questionBack.categoryId);
            questionBackMapper.modifyQuestionBackNameById(questionBackEntity);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public PageInfo<StudentQuestionAnswer> getStudentAnswerByAssignment(String userId, String assignmentId, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<StudentQuestionAnswer> list = questionBackMapper.getStudentAnswerByAssignment(userId, assignmentId);
        return new PageInfo<>(list);
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

        long zeroScoreCount = questionBackAssignmentList.stream().filter(x->x.score==0).count();
        long scoreSum = questionBackAssignmentList.stream().mapToInt(x->x.score).sum();
        int perScore = Math.toIntExact((100 - scoreSum) / zeroScoreCount);
        int leftScore = 100 - Math.toIntExact(scoreSum + perScore * zeroScoreCount);
        questionBackAssignmentList.stream().filter(x->x.score==0).collect(Collectors.toList()).forEach(x -> {
            if(questionBackAssignmentList.stream().filter(a->a.score==0).collect(Collectors.toList()).indexOf(x)==zeroScoreCount-1)
            {
                x.score = perScore + leftScore;
            }
            x.score = perScore;
        });
        questionBackAssignmentList.forEach(x -> {
            QuestionBackAssignmentEntity questionBackAssignmentEntity = new QuestionBackAssignmentEntity();
            questionBackAssignmentEntity.setAssignmentId(x.assignmentId);
            questionBackAssignmentEntity.setQuestionId(x.questionId);
            questionBackAssignmentEntity.setScore(x.score);
            questionBackAssignmentEntityList.add(questionBackAssignmentEntity);
        });
        try {
            String assignmentId = questionBackAssignmentEntityList.get(0).getAssignmentId();
            questionBackMapper.deleteQuestionBackAssignmentByAssignmentId(assignmentId);
            questionBackMapper.addQuestionBackAssignmentList(questionBackAssignmentEntityList);
        } catch (Exception e) {
            return questionBackAssignmentEntityList;
        }
        return questionBackAssignmentEntityList;
    }

}
