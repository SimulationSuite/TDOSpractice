package org.tdos.tdospractice.controller;

import com.github.pagehelper.PageInfo;
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
import org.tdos.tdospractice.type.StudentQuestionAnswer;

import java.util.*;

@RestController
public class QuestionBackController {

    @Autowired
    private QuestionBackService questionBackService;

    @GetMapping(value = "/getQuestionBackAll")
    public Response<PageInfo<QuestionBackEntity>> getQuestionBackAll(@RequestParam(value = "type", required = false) Integer type,
                                                                     @RequestParam(value = "content", required = false) String content,
                                                                     @RequestParam(value = "category_id", required = false) String categoryId,
                                                                     @RequestParam(value = "assignment_id", required = false) String assignmentId,
                                                                     @RequestParam(value = "perPage") Integer perPage,
                                                                     @RequestParam(value = "page") Integer page) {
        return Response.success(questionBackService.getQuestionBackAll(type, content, categoryId, assignmentId, perPage, page));
    }

    @GetMapping(value = "/getStudentAnswerByAssignment")
    public Response<PageInfo<StudentQuestionAnswer>> getStudentAnswerByAssignment(@RequestParam(value = "userId") String userId,
                                                                                  @RequestParam(value = "assignmentId") String assignmentId,
                                                                                  @RequestParam(value = "perPage") Integer perPage,
                                                                                  @RequestParam(value = "page") Integer page) {
        return Response.success(questionBackService.getStudentAnswerByAssignment(userId, assignmentId, perPage, page));
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

