package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.DeleteIdList;
import org.tdos.tdospractice.entity.StudentAnswerEntity;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.StudentAnswerService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.StudentAnswer;
import org.tdos.tdospractice.body.StudentAnswerList;

import java.util.*;

@RestController
public class StudentAnswerController {

    @Autowired
    private StudentAnswerService studentAnswerService;

    @GetMapping(value = "/getStudentAnswerByAssignmentUserId")
    public Response<PageInfo<StudentAnswerEntity>> getStudentAnswerByAssignmentUserId(@RequestParam(value = "userId") String userId,
                                                                                      @RequestParam(value = "assignmentId") String assignmentId,
                                                                                      @RequestParam(value = "perPage") Integer perPage,
                                                                                      @RequestParam(value = "page") Integer page) {
        return Response.success(studentAnswerService.getStudentAnswerByAssignmentUserId(userId, assignmentId, perPage, page));
    }

    @GetMapping(value = "/getStudentAnswerByCourseId")
    public Response<PageInfo<StudentAnswerEntity>> getStudentAnswerByCourseId(@RequestParam(value = "courseId") String courseId,
                                                                              @RequestParam(value = "perPage") Integer perPage,
                                                                              @RequestParam(value = "page") Integer page) {
        return Response.success(studentAnswerService.getStudentAnswerBySectionId(courseId, perPage, page));
    }

    @GetMapping(value = "/getStudentAnswerByChapterId")
    public Response<PageInfo<StudentAnswerEntity>> getStudentAnswerByChapterId(@RequestParam(value = "chapterId") String chapterId,
                                                                               @RequestParam(value = "perPage") Integer perPage,
                                                                               @RequestParam(value = "page") Integer page) {
        return Response.success(studentAnswerService.getStudentAnswerBySectionId(chapterId, perPage, page));
    }

    @GetMapping(value = "/getStudentAnswerBySectionId")
    public Response<PageInfo<StudentAnswerEntity>> getStudentAnswerBySectionId(@RequestParam(value = "sectionId") String sectionId,
                                                                               @RequestParam(value = "perPage") Integer perPage,
                                                                               @RequestParam(value = "page") Integer page) {
        return Response.success(studentAnswerService.getStudentAnswerBySectionId(sectionId, perPage, page));
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

    @PostMapping(value = "/addStudentAnswerList")
    public Response<List<StudentAnswerEntity>> addStudentAnswerList(@RequestBody StudentAnswerList studentAnswerList) {
        return Response.success(studentAnswerService.addStudentAnswerList(studentAnswerList.studentAnswerList));
    }

}

