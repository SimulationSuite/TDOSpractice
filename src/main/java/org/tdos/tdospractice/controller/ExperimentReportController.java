package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.entity.ExperimentReportEntity;
import org.tdos.tdospractice.service.ExperimentReportService;
import org.tdos.tdospractice.type.Response;

import java.time.LocalDateTime;

@RestController
public class ExperimentReportController {


    @Autowired
    private ExperimentReportService experimentReportService;

    @PostMapping(value = "/insertExperimentRepor")
    public Response insertExperimentRepor(@RequestBody ExperimentReportEntity experimentReportEntity) {
        try {
            if (experimentReportService.findExperimentReportByExperimentAndUserId(experimentReportEntity.getExperiment_id(), experimentReportEntity.getUser_id()).isPresent()) {
                if (experimentReportService.updateExperimentReport(experimentReportEntity)) {
                    return Response.success();
                }
                return Response.error("新增失败");
            }
            int i = experimentReportService.insert(experimentReportEntity);
            if (i == 1)
                return Response.success();
            return Response.error("新增失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }

    }

    @PostMapping(value = "/updateExperimentReport")
    public Response updateExperimentReport(@RequestBody ExperimentReportEntity experimentReportEntity) {
        try {
            if (experimentReportEntity.getStatus() == 1)
                experimentReportEntity.setSubmit_at(LocalDateTime.now());
            if (experimentReportService.updateExperimentReport(experimentReportEntity))
                return Response.success();
            return Response.error("更新失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @GetMapping(value = "/findByExperimentReport")
    public Response findByExperimentReport(@ModelAttribute ExperimentReportEntity experimentReportEntity) {
        return Response.success(experimentReportService.findByExperimentReport(experimentReportEntity));
    }

    @GetMapping(value = "/hasExperimentReport")
    public Response hasExperimentReport(@RequestParam(value = "experiment_id") String experiment_id,
                                        @RequestParam(value = "user_id") String user_id) {
        if (experimentReportService.findExperimentReportByExperimentAndUserId(experiment_id, user_id).isPresent()) {
            return Response.success("该实验报告已存在");
        }
        return Response.error("该实验报告不存在");
    }

    @GetMapping(value = "/findByExperimentReportAll")
    public Response findByExperimentReportAll(@RequestParam(value = "course_id") String course_id,
                                              @RequestParam(value = "status") int status,
                                              @RequestParam(value = "isCorrect") int isCorrect,
                                              @RequestParam(value = "name") String name,
                                              @RequestParam(value = "startTime") String startTime,
                                              @RequestParam(value = "endTime") String endTime,
                                              @RequestParam(value = "user_id") String user_id,
                                              @RequestParam(value = "perPage") Integer perPage,
                                              @RequestParam(value = "page") Integer page) {
        return Response.success(experimentReportService.findExperimentReportAll(course_id, status, isCorrect, name, startTime, endTime,user_id, perPage, page));
    }

    @GetMapping(value = "/findExperimentReportByExperimentAndUserId")
    public Response findExperimentReportByExperimentAndUserId(@RequestParam(value = "experiment_id") String experiment_id,
                                                              @RequestParam(value = "user_id") String user_id) {
        return Response.success(experimentReportService.findExperimentReportByExperimentAndUserId(experiment_id, user_id).get());
    }
}
