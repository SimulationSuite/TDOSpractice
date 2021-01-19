package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.DeleteIdList;
import org.tdos.tdospractice.entity.StudentScoreEntity;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.service.SecurityService;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.StudentScoreService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.StudentScore;

import java.util.*;

@RestController
public class StudentScoreController {

    @Autowired
    private StudentScoreService studentScoreService;

    @Autowired
    private ClassService classService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = "/getStudentScoreBySectionId")
    public Response<List<StudentScoreEntity>> getStudentScoreBySectionId(@RequestParam(value = "sectionId") String sectionId) {
        return Response.success(studentScoreService.getStudentScoreBySectionId(sectionId));
    }

    @PostMapping(value = "/deleteStudentScoreById")
    public Response<Map<String, Object>> deleteStudentScoreById(@RequestBody DeleteIdList idList) {
        Map<String, Object> map = studentScoreService.deleteStudentScoreById(idList.deleteIdList);
        return Response.success(map);
    }

    @PostMapping(value = "/addStudentScore")
    public Response<StudentScoreEntity> addStudentScore(@RequestBody StudentScore studentScore) {
        return Response.success(studentScoreService.addStudentScore(studentScore));
    }

    @PostMapping(value = "/modifyStudentScoreById")
    public Response<Boolean> modifyStudentScoreById(@RequestBody StudentScore studentScore) {
        return Response.success(studentScoreService.modifyStudentScoreById(studentScore));
    }

    @GetMapping(value = "/getStudentScoreAll")
    public Response<List<StudentScoreEntity>> getStudentScoreAll() {
        return Response.success(studentScoreService.getStudentScoreAll());
    }

}

