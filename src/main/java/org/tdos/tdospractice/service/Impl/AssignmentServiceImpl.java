package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.tdos.tdospractice.body.Assignment;
import org.tdos.tdospractice.body.StudentScore;
import org.tdos.tdospractice.entity.AssignmentEntity;
import org.tdos.tdospractice.entity.StudentAnswerEntity;
import org.tdos.tdospractice.entity.StudentScoreEntity;
import org.tdos.tdospractice.mapper.AssignmentMapper;
import org.tdos.tdospractice.mapper.StudentAnswerMapper;
import org.tdos.tdospractice.mapper.StudentScoreMapper;
import org.tdos.tdospractice.service.AssignmentService;
import org.tdos.tdospractice.type.AssignmentQuestionBack;
import org.tdos.tdospractice.type.AssignmentStatistics;
import org.tdos.tdospractice.type.StudentAssignment;
import org.tdos.tdospractice.utils.UTCTimeUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private StudentAnswerMapper studentAnswerMapper;

    @Autowired
    private StudentScoreMapper studentScoreMapper;

    @Override
    public PageInfo<StudentAssignment> getStudentAssignment(String userId, String courseId,String chapterId, String sectionId, Integer status,String name, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<StudentAssignment> list = assignmentMapper.getStudentAssignment(userId, courseId, chapterId, sectionId, name);
        if(status != null)
        {
            list = list.stream().filter(x -> x.getStatus() == status).collect(Collectors.toList());
        }
        return new PageInfo<>(list);
    }

    @Override
    public AssignmentStatistics getAssignmentStatisticsBySectionId(String sectionId) {
        AssignmentStatistics assignmentStatistics = new AssignmentStatistics();
        List<StudentAnswerEntity> allStudentAssignmentList = assignmentMapper.getAllStudentAssignmentBySectionId(sectionId);
        List<StudentAnswerEntity> subStudentAssignmentList = assignmentMapper.getSubStudentAssignmentBySectionId(sectionId);
        assignmentStatistics.total = (int) allStudentAssignmentList.stream().count();
        assignmentStatistics.committed = (int) subStudentAssignmentList.stream().count();
        assignmentStatistics.uncommitted = assignmentStatistics.total - assignmentStatistics.committed;
        allStudentAssignmentList.removeAll(subStudentAssignmentList);
        assignmentStatistics.uncommittedList = allStudentAssignmentList;
        return assignmentStatistics;
    }

    @Override
    public PageInfo<StudentAssignment> getAssignmentAll(String classId, String courseId, String chapterId, String sectionId, Integer status, String name,String startTime,String endTime, Integer perPage, Integer page,String ownerId) {
        PageHelper.startPage(page, perPage);
        List<StudentAssignment> list = assignmentMapper.getAssignmentAll(classId, courseId, chapterId, sectionId, name, startTime, endTime, ownerId);
        if(status != null)
        {
            list = list.stream().filter(x -> x.getStatus() == status).collect(Collectors.toList());
        }
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
    public PageInfo<AssignmentQuestionBack> getAssignmentBySectionId(String sectionId, Integer type, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<AssignmentQuestionBack> list = assignmentMapper.getAssignmentBySectionId(sectionId, type);
        return new PageInfo<>(list);
    }

    @Override
    public AssignmentEntity getAssignmentNameBySectionId(String sectionId) {
        if(assignmentMapper.ifAssignmentBySectionId(sectionId))
        {
            AssignmentEntity assignmentEntity = assignmentMapper.getAssignmentNameBySectionId(sectionId);
            return assignmentEntity;
        }
        return new AssignmentEntity();
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
    public Pair<Boolean, Object> addAssignment(Assignment assignment) {
        if (ObjectUtils.isEmpty(assignment.sectionId)) {
            return new Pair<>(false, "作业绑定的节不能为空。");
        }
        if (ObjectUtils.isEmpty(assignment.endAt)) {
            return new Pair<>(false, "作业截至不能为空。");
        }
        if (assignment.qualifiedScore != 100) {
            return new Pair<>(false, "作业分数不为100。");
        }
        if (ObjectUtils.isEmpty(assignment.name)) {
            return new Pair<>(false, "作业名称不能为空。");
        }if (assignmentMapper.hasAssignmentNameExist(assignment.name) > 0) {
            return new Pair<>(false, "作业名称已存在。");
        }
        AssignmentEntity assignmentEntity = new AssignmentEntity();
        assignmentEntity.setSectionId(assignment.sectionId);
        assignmentEntity.setName(assignment.name);
        assignmentEntity.setEndAt(assignment.endAt);
        assignmentEntity.setQualifiedScore(assignment.qualifiedScore);
        try {
            assignmentMapper.addAssignment(assignmentEntity);
        } catch (Exception e) {
            return new Pair<>(false, e);
        }
        return new Pair<>(true, assignmentEntity);
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

    @Override
    public Boolean modifyAssignmentStatusById(Assignment assignment) {
        try {
            assignmentMapper.modifyAssignmentStatusById(assignment.id, assignment.status);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void updateEndAssignment() {
        try {
            String committedTime = UTCTimeUtils.getUTCTimeStr();
            List<AssignmentEntity> endAssignmentEntityList = assignmentMapper.getEndAssignment(committedTime);
            DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            endAssignmentEntityList.forEach(x -> {
                String assignmentId = x.getId();
                List<String> userIdList = assignmentMapper.getUsers(assignmentId);
                userIdList.forEach(u -> {
                    if(assignmentMapper.ifStudentAnswer(u, assignmentId)){
                        studentAnswerMapper.modifyStudentAnswerStatus(1, committedTime, assignmentId, u);
                    }else{
                        List<StudentAnswerEntity> newStudentAnswerEntity = assignmentMapper.getQuestionBackByAssignment(assignmentId);
                        newStudentAnswerEntity.forEach(n -> {
                            n.setUserId(u);
                            n.setCommittedAt(LocalDateTime.parse(committedTime, timeDtf));
                        });
                        studentAnswerMapper.addStudentAnswerList(newStudentAnswerEntity);
                        StudentScoreEntity studentScoreEntity = new StudentScoreEntity();
                        studentScoreEntity.setUserId(u);
                        studentScoreEntity.setAssignmentId(assignmentId);
                        studentScoreEntity.setStatus(1);
                        studentScoreEntity.setScore(0);
                        studentScoreMapper.addStudentScore(studentScoreEntity);
                    };
                });
            });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public Map<String, Object> deleteQuestionBackAssignmentById(List<String> id) {
        Map<String, Object> map = new HashMap<>();
        try{
            id.forEach(x -> assignmentMapper.deleteQuestionBackAssignmentById(x));
            map.put("isDelete", true);
        }
        catch (Exception e)
        {
            map.put("isDelete", false);
            map.put("reason", e.toString());
        }
        return map;
    }

}
