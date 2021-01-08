package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.AssignmentIdList;
import org.tdos.tdospractice.body.Courseware;
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

