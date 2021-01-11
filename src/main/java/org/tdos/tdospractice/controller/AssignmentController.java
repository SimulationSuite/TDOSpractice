package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.AssignmentIdList;
import org.tdos.tdospractice.entity.AssignmentEntity;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.service.SecurityService;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.AssignmentService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.Assignment;

import java.util.*;

@RestController
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private ClassService classService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = "/getAssignmentByClassId")
    public Response<List<AssignmentEntity>> getAssignmentByClassId(@RequestParam(value = "classId") String classId) {
        return Response.success(assignmentService.getAssignmentByClassId(classId));
    }

    @GetMapping(value = "/getAssignmentByCourseId")
    public Response<List<AssignmentEntity>> getAssignmentByCourseId(@RequestParam(value = "courseId") String courseId) {
        return Response.success(assignmentService.getAssignmentByCourseId(courseId));
    }

    @GetMapping(value = "/getAssignmentByChapterId")
    public Response<List<AssignmentEntity>> getAssignmentByChapterId(@RequestParam(value = "chapterId") String chapterId) {
        return Response.success(assignmentService.getAssignmentByChapterId(chapterId));
    }

    @GetMapping(value = "/getAssignmentBySectionId")
    public Response<List<AssignmentEntity>> getAssignmentBySectionId(@RequestParam(value = "sectionId") String sectionId) {
        return Response.success(assignmentService.getAssignmentBySectionId(sectionId));
    }

    @PostMapping(value = "/deleteAssignmentById")
    public Response<Map<String, Object>> deleteAssignmentById(@RequestBody AssignmentIdList idList) {
        Map<String, Object> map = assignmentService.deleteAssignmentById(idList.assignmentIdList);
        return Response.success(map);
    }

    @PostMapping(value = "/addAssignment")
    public Response<AssignmentEntity> addAssignment(@RequestBody Assignment assignment) {
        return Response.success(assignmentService.addAssignment(assignment));
    }

    @PostMapping(value = "/modifyAssignmentNameById")
    public Response<Boolean> modifyAssignmentNameById(@RequestBody Assignment assignment) {
        return Response.success(assignmentService.modifyAssignmentNameById(assignment));
    }

}

