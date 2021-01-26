package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.Assignment;
import org.tdos.tdospractice.entity.AssignmentEntity;
import org.tdos.tdospractice.entity.StudentAnswerEntity;
import org.tdos.tdospractice.mapper.AssignmentMapper;
import org.tdos.tdospractice.service.AssignmentService;

import java.util.*;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Override
    public PageInfo<StudentAnswerEntity> getAssignmentAll(String classId,String courseId,String chapterId, String sectionId, String userId, Integer status,String name, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<StudentAnswerEntity> list = assignmentMapper.getAssignmentAll(classId, courseId, chapterId, sectionId, userId, status, name);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AssignmentEntity> getAssignmentByClassId(String classId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<AssignmentEntity> list = assignmentMapper.getAssignmentByClassId(classId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AssignmentEntity> getAssignmentByCourseId(String courseId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<AssignmentEntity> list = assignmentMapper.getAssignmentByCourseId(courseId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AssignmentEntity> getAssignmentByChapterId(String chapterId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<AssignmentEntity> list = assignmentMapper.getAssignmentByChapterId(chapterId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AssignmentEntity> getAssignmentBySectionId(String sectionId, Integer perPage,Integer page) {
        PageHelper.startPage(page, perPage);
        List<AssignmentEntity> list = assignmentMapper.getAssignmentBySectionId(sectionId);
        return new PageInfo<>(list);
    }

    @Override
    public Map<String, Object> deleteAssignmentById(List<String> id) {
        Map<String, Object> map = new HashMap<>();
        List<String> sectionAssignment = new ArrayList<>();
        id.forEach(x -> {
            if (!assignmentMapper.ifSectionAssignmentByAssignmentId(x)){
                sectionAssignment.add(x);
            }
        });
        if (sectionAssignment.size() > 0){
            map.put("isDelete", false);
            map.put("notDeleteId", sectionAssignment);
            return map;
        }
        id.forEach(x -> assignmentMapper.deleteAssignmentById(x));
        map.put("isDelete", true);
        map.put("notDeleteId", sectionAssignment);
        return map;
    }

    @Override
    public AssignmentEntity addAssignment(Assignment assignment) {
        AssignmentEntity assignmentEntity = new AssignmentEntity();
        assignmentEntity.setSectionId(assignment.sectionId);
        assignmentEntity.setName(assignment.name);
        assignmentEntity.setEndAt(assignment.endAt);
        assignmentEntity.setQualifiedScore(assignment.qualifiedScore);
        try {
            assignmentMapper.addAssignment(assignmentEntity);
        } catch (Exception e) {
            return assignmentEntity;
        }
        return assignmentEntity;
    }

    @Override
    public Boolean modifyAssignmentNameById(Assignment assignment) {
        try {
            assignmentMapper.modifyAssignmentNameById(assignment.id, assignment.name);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
