package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.entity.ExperimentEntity;
import org.tdos.tdospractice.service.ExperimentService;
import org.tdos.tdospractice.type.Response;

import java.util.List;


@RestController
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;

    @PostMapping(value = "/insertExperiment")
    public Response insertExperiment(@RequestBody ExperimentEntity experimentEntity) {
        try {
            int i = experimentService.insert(experimentEntity);
            if (i == 1)
                return Response.success();
            return Response.error("新增失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }

    }

    @GetMapping(value = "/findAllByCourseId")
    public Response findAllByCourseId(@RequestParam(value = "course_id") String course_id) {
        return Response.success(experimentService.findAllByCourseId(course_id));
    }

    @GetMapping(value = "/findAllByChapterId")
    public Response findAllByChapterId(@RequestParam(value = "chapter_id") String chapter_id) {
        return Response.success(experimentService.findAllByChapterId(chapter_id));
    }

    @GetMapping(value = "/findAllBySectionId")
    public Response findAllBySectionId(@RequestParam(value = "section_id") String section_id) {
        return Response.success(experimentService.findAllBySectionId(section_id));
    }

    @GetMapping(value = "/findAllByCategoryId")
    public Response findAllByCategoryId(@RequestParam(value = "category_id") String category_id) {
        return Response.success(experimentService.findAllByCategoryId(category_id));
    }


    @PostMapping(value = "/updateExperiment")
    public Response updateExperiment(@RequestBody ExperimentEntity experimentEntity) {
        try {
            if (experimentService.updateExperiment(experimentEntity))
                return Response.success();
            return Response.error("更新失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @PostMapping(value = "/deleteExperiment")
    public Response deleteExperiment(@RequestParam(value = "id") String id) {
        try {
            ExperimentEntity experimentEntity = experimentService.findByID(id);
            if (experimentService.deleteExperiment(id))
                return Response.success();
            return Response.error("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }
}
