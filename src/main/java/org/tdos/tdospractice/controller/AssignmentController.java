package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.AssignmentIdList;
import org.tdos.tdospractice.entity.AssignmentEntity;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.AssignmentService;
import org.tdos.tdospractice.type.AssignmentQuestionBack;
import org.tdos.tdospractice.type.AssignmentStatistics;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.Assignment;
import org.tdos.tdospractice.type.StudentAssignment;

import java.util.*;

@RestController
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping(value = "/getStudentAssignment")
    public Response<PageInfo<StudentAssignment>> getStudentAssignment(@RequestParam(value = "userId") String userId,
                                                                  @RequestParam(value = "courseId",required = false) String courseId,
                                                                  @RequestParam(value = "chapterId",required = false) String chapterId,
                                                                  @RequestParam(value = "sectionId",required = false) String sectionId,
                                                                  @RequestParam(value = "status",required = false) Integer status,
                                                                  @RequestParam(value = "name",required = false) String name,
                                                                  @RequestParam(value = "perPage") Integer perPage,
                                                                  @RequestParam(value = "page") Integer page) {
        return Response.success(assignmentService.getStudentAssignment(userId, courseId, chapterId, sectionId, status, name, perPage, page));
    }

    @GetMapping(value = "/getAssignmentStatisticsBySectionId")
    public Response<AssignmentStatistics> getAssignmentStatisticsBySectionId(@RequestParam(value = "sectionId") String sectionId) {
        return Response.success(assignmentService.getAssignmentStatisticsBySectionId(sectionId));
    }

    @GetMapping(value = "/getAssignmentAll")
    public Response<PageInfo<StudentAssignment>> getAssignmentAll(@RequestParam(value = "classId",required = false) String classId,
                                                                  @RequestParam(value = "courseId",required = false) String courseId,
                                                                  @RequestParam(value = "chapterId",required = false) String chapterId,
                                                                  @RequestParam(value = "sectionId",required = false) String sectionId,
                                                                  @RequestParam(value = "status",required = false) Integer status,
                                                                  @RequestParam(value = "name",required = false) String name,
                                                                  @RequestParam(value = "startTime",required = false) String startTime,
                                                                  @RequestParam(value = "endTime",required = false) String endTime,
                                                                  @RequestParam(value = "perPage") Integer perPage,
                                                                  @RequestParam(value = "page") Integer page) {
        return Response.success(assignmentService.getAssignmentAll(classId, courseId, chapterId, sectionId, status, name, startTime, endTime, perPage, page));
    }

    @GetMapping(value = "/getAssignmentByClassId")
    public Response<PageInfo<AssignmentEntity>> getAssignmentByClassId(@RequestParam(value = "classId") String classId,
                                                                       @RequestParam(value = "perPage") Integer perPage,
                                                                       @RequestParam(value = "page") Integer page) {
        return Response.success(assignmentService.getAssignmentByClassId(classId, perPage, page));
    }

    @GetMapping(value = "/getAssignmentByCourseId")
    public Response<PageInfo<AssignmentEntity>> getAssignmentByCourseId(@RequestParam(value = "courseId") String courseId,
                                                                        @RequestParam(value = "perPage") Integer perPage,
                                                                        @RequestParam(value = "page") Integer page) {
        return Response.success(assignmentService.getAssignmentByCourseId(courseId, perPage, page));
    }

    @GetMapping(value = "/getAssignmentByChapterId")
    public Response<PageInfo<AssignmentEntity>> getAssignmentByChapterId(@RequestParam(value = "chapterId") String chapterId,
                                                                         @RequestParam(value = "perPage") Integer perPage,
                                                                         @RequestParam(value = "page") Integer page) {
        return Response.success(assignmentService.getAssignmentByChapterId(chapterId, perPage, page));
    }

    @GetMapping(value = "/getAssignmentBySectionId")
    public Response<PageInfo<AssignmentQuestionBack>> getAssignmentBySectionId(@RequestParam(value = "sectionId") String sectionId,
                                                                               @RequestParam(value = "perPage") Integer perPage,
                                                                               @RequestParam(value = "page") Integer page) {
        return Response.success(assignmentService.getAssignmentBySectionId(sectionId, perPage, page));
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

