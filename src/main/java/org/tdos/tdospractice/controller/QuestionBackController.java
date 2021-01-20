package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.DeleteIdList;
import org.tdos.tdospractice.entity.QuestionBackAssignmentEntity;
import org.tdos.tdospractice.entity.QuestionBackEntity;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.service.SecurityService;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.QuestionBackService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.QuestionBack;
import org.tdos.tdospractice.body.QuestionBackAssignment;
import org.tdos.tdospractice.body.QuestionBackAssignmentList;

import java.util.*;

@RestController
public class QuestionBackController {

    @Autowired
    private QuestionBackService questionBackService;

    @GetMapping(value = "/getStudentAnswerByAssignment")
    public Response<QuestionBackEntity> getStudentAnswerByAssignment(@RequestParam(value = "assignmentId") String assignmentId) {
        return Response.success(questionBackService.getStudentAnswerByAssignment(assignmentId));
    }

    @PostMapping(value = "/deleteQuestionBackById")
    public Response<Map<String, Object>> deleteQuestionBackById(@RequestBody DeleteIdList idList) {
        Map<String, Object> map = questionBackService.deleteQuestionBackById(idList.deleteIdList);
        return Response.success(map);
    }

    @PostMapping(value = "/addQuestionBack")
    public Response<QuestionBackEntity> addQuestionBack(@RequestBody QuestionBack qestionBack) {
        return Response.success(questionBackService.addQuestionBack(qestionBack));
    }

    @PostMapping(value = "/modifyQuestionBackById")
    public Response<Boolean> modifyQuestionBackById(@RequestBody QuestionBack qestionBack) {
        return Response.success(questionBackService.modifyQuestionBackById(qestionBack));
    }

    @PostMapping(value = "/addQuestionBackAssignmentList")
    public Response<List<QuestionBackAssignmentEntity>> addQuestionBackAssignmentList(@RequestBody QuestionBackAssignmentList questionBackAssignmentList) {
        return Response.success(questionBackService.addQuestionBackAssignmentList(questionBackAssignmentList.questionBackAssignmentList));
    }

}

