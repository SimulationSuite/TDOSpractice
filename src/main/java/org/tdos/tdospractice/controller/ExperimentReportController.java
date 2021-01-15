package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.entity.ExperimentReportEntity;
import org.tdos.tdospractice.service.ExperimentReportService;
import org.tdos.tdospractice.type.Response;

//@RestController
public class ExperimentReportController {


    private ExperimentReportService experimentReportService;

    @PostMapping(value = "/insertExperimentRepor")
    public Response insertExperimentRepor(@RequestBody ExperimentReportEntity experimentReportEntity) {
        try {
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
}
