package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.DeleteIdList;
import org.tdos.tdospractice.entity.StudentAnswerEntity;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.service.SecurityService;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.StudentAnswerService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.StudentAnswer;

import java.util.*;

@RestController
public class StudentAnswerController {

    @Autowired
    private StudentAnswerService studentAnswerService;

    @Autowired
    private ClassService classService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = "/getStudentAnswerBySectionId")
    public Response<List<StudentAnswerEntity>> getStudentAnswerBySectionId(@RequestParam(value = "sectionId") String sectionId) {
        return Response.success(studentAnswerService.getStudentAnswerBySectionId(sectionId));
    }

    @PostMapping(value = "/deleteStudentAnswerById")
    public Response<Map<String, Object>> deleteStudentAnswerById(@RequestBody DeleteIdList idList) {
        Map<String, Object> map = studentAnswerService.deleteStudentAnswerById(idList.deleteIdList);
        return Response.success(map);
    }

    @PostMapping(value = "/addStudentAnswer")
    public Response<StudentAnswerEntity> addStudentAnswer(@RequestBody StudentAnswer studentAnswer) {
        return Response.success(studentAnswerService.addStudentAnswer(studentAnswer));
    }

    @PostMapping(value = "/modifyStudentAnswerById")
    public Response<Boolean> modifyStudentAnswerById(@RequestBody StudentAnswer studentAnswer) {
        return Response.success(studentAnswerService.modifyStudentAnswerById(studentAnswer));
    }

}

