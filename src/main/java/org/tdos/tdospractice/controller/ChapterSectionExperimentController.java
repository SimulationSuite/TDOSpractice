package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.body.BindExperiments;
import org.tdos.tdospractice.entity.ChapterSectionExperimentEntity;
import org.tdos.tdospractice.service.ChapterSectionExperimentService;
import org.tdos.tdospractice.type.Response;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChapterSectionExperimentController {

    @Autowired
    private ChapterSectionExperimentService chapterSectionExperimentService;

    @PostMapping(value = "/bindExperiments")
    public Response insertExperiment(@RequestBody BindExperiments bindExperiments) {
        try {
            List<ChapterSectionExperimentEntity> list = new ArrayList<>();
            bindExperiments.getExperiment_id().forEach(b ->{
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
    public Response unbindExperiments(@RequestParam(value = "id") String id) {
        Pair<Boolean,String> pair = chapterSectionExperimentService.deleteChapterSectionExperiment(id);
        if (pair.getKey()){
            return Response.success();
        }else {
            return Response.error("删除失败");
        }
    }
}
