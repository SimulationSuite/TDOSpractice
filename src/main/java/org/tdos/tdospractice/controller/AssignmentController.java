package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.AssignmentIdList;
import org.tdos.tdospractice.entity.AssignmentEntity;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.entity.StudentAnswerEntity;
import org.tdos.tdospractice.service.AssignmentService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.Assignment;

import java.util.*;

@RestController
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping(value = "/getAssignmentAll")
    public Response<PageInfo<StudentAnswerEntity>> getAssignmentAll(@RequestParam(value = "classId",required = false) String classId,
                                                                    @RequestParam(value = "courseId",required = false) String courseId,
                                                                    @RequestParam(value = "chapterId",required = false) String chapterId,
                                                                    @RequestParam(value = "sectionId",required = false) String sectionId,
                                                                    @RequestParam(value = "userId",required = false) String userId,
                                                                    @RequestParam(value = "status",required = false) Integer status,
                                                                    @RequestParam(value = "name",required = false) String name,
                                                                    @RequestParam(value = "perPage") Integer perPage,
                                                                    @RequestParam(value = "page") Integer page) {
        return Response.success(assignmentService.getAssignmentAll(classId, courseId, chapterId, sectionId, userId, status, name, perPage, page));
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
    public Response<PageInfo<AssignmentEntity>> getAssignmentBySectionId(@RequestParam(value = "sectionId") String sectionId,
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

