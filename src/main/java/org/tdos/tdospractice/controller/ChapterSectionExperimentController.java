package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.body.BindExperiments;
import org.tdos.tdospractice.entity.ChapterSectionExperimentEntity;
import org.tdos.tdospractice.entity.ExperimentEntity;
import org.tdos.tdospractice.entity.ExperimentImageEntity;
import org.tdos.tdospractice.mapper.ExperimentMapper;
import org.tdos.tdospractice.service.ChapterSectionExperimentService;
import org.tdos.tdospractice.service.ExperimentImageService;
import org.tdos.tdospractice.type.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChapterSectionExperimentController {

    @Autowired
    private ChapterSectionExperimentService chapterSectionExperimentService;

    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private ExperimentImageService experimentImageService;

    @PostMapping(value = "/bindExperiments")
    public Response insertExperiment(@RequestBody BindExperiments bindExperiments) {
        try {
            List<String> ids = new ArrayList<>();
            bindExperiments.getExperiment_id().stream().forEach(id -> {
                ExperimentEntity experimentEntity = experimentMapper.findById(id);
                experimentEntity.setType(1);
                experimentEntity.setParent_id(id);
                experimentMapper.insert(experimentEntity);
                List<ExperimentImageEntity> list = new ArrayList<>();
                experimentImageService.findImageByExperiment(id).stream().forEach(images -> {
                    list.add(ExperimentImageEntity
                            .builder()
                            .experiment_id(experimentEntity.getId())
                            .image_id(images.getImage_id())
                            .build());
                });
                experimentImageService.insertExperimentImages(list);
                ids.add(experimentEntity.getId());
            });
            List<ChapterSectionExperimentEntity> list = new ArrayList<>();
            ids.forEach(b -> {
                list.add(ChapterSectionExperimentEntity
                        .builder()
                        .experiment_id(b)
                        .section_id(bindExperiments.getSection_id()).build());
            });
            int i = chapterSectionExperimentService.insert(list);
            if (i > 0)
                return Response.success();
            return Response.error("新增失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @PostMapping(value = "/unbindExperiments")
    public Response unbindExperiments(@RequestParam(value = "section_id") String section_id, @RequestParam(value = "experiment_id") String experiment_id) {
        Pair<Boolean, String> pair = chapterSectionExperimentService.deleteChapterSectionExperiment(section_id, experiment_id);
        experimentMapper.deleteExperiment(experiment_id);
        if (pair.getKey()) {
            return Response.success();
        } else {
            return Response.error("删除失败");
        }
    }
}
