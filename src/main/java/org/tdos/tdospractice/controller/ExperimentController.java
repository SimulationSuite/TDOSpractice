package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.entity.CategoryEntity;
import org.tdos.tdospractice.entity.ChapterSectionExperimentEntity;
import org.tdos.tdospractice.entity.ExperimentEntity;
import org.tdos.tdospractice.entity.ExperimentImageEntity;
import org.tdos.tdospractice.service.*;
import org.tdos.tdospractice.type.Course;
import org.tdos.tdospractice.type.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExperimentImageService experimentImageService;

    @Autowired
    private ChapterSectionExperimentService chapterSectionExperimentService;

    @PostMapping(value = "/insertExperiment")
    public Response insertExperiment(@RequestBody ExperimentEntity experimentEntity) {
        try {
            int i = experimentService.insert(experimentEntity);
            List<ExperimentImageEntity> list = new ArrayList<>();
            experimentEntity.getImages().forEach(image -> {
                list.add(
                        ExperimentImageEntity
                                .builder()
                                .experiment_id(experimentEntity.getId())
                                .image_id(image)
                                .build());
            });
            int j = experimentImageService.insertExperimentImages(list);
            if (i == 1 && j == list.size())
                return Response.success();
            if (i == -1)
                return Response.error("该实验已存在");
            return Response.error("新增失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }

    }

    @GetMapping(value = "/findExperimentByCategory")
    public Response findExperimentByCategory(@RequestParam(value = "category_id") String category_id,
                                             @RequestParam(value = "name") String name,
                                             @RequestParam(value = "perPage") Integer perPage,
                                             @RequestParam(value = "page") Integer page) {
        List<String> list = new ArrayList<>();
        if (category_id.equals("")) {
            return Response.success(experimentService.findExperiment(list, name, perPage, page));
        } else {
            Optional<CategoryEntity> categoryEntity = categoryService.findCategory(category_id);
            if (categoryEntity.isPresent()) {
                if (categoryEntity.get().getParent_category_id() == null) {
                    categoryService.findChildCategory(category_id).forEach(c -> {
                        list.add(c.getId());
                    });
                } else {
                    list.add(category_id);
                }
                return Response.success(experimentService.findExperiment(list, name, perPage, page));
            }
        }
        return Response.error("找不到该分类");
    }

    @GetMapping(value = "/findSelectedExperimentByCategory")
    public Response findExperimentByCategory(@RequestParam(value = "f_category_id") String f_category_id,
                                             @RequestParam(value = "c_category_id") String c_category_id,
                                             @RequestParam(value = "section_id") String section_id,
                                             @RequestParam(value = "name") String name,
                                             @RequestParam(value = "perPage") Integer perPage,
                                             @RequestParam(value = "page") Integer page) {
        return Response.success(experimentService.findSelectedExperimentByCategory(f_category_id, c_category_id, section_id, name, perPage, page));
    }


    @GetMapping(value = "/findAllByType")
    public Response findAllByType(@RequestParam(value = "id") String id,
                                  @RequestParam(value = "type") int type,
                                  @RequestParam(value = "perPage") Integer perPage,
                                  @RequestParam(value = "page") Integer page) {
        if (type == 0) {
            return Response.success(experimentService.findById(id));
        } else if (type == 1) {//通过课程查询
            return Response.success(experimentService.findAllByCourseId(id, perPage, page));
        } else if (type == 2) {//通过章查询
            return Response.success(experimentService.findAllByChapterId(id, perPage, page));
        } else if (type == 3) {//通过节查询
            return Response.success(experimentService.findAllBySectionId(id, perPage, page));
        }
        return Response.error("类型错误");
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
            //TODO 判断课程是否已发布
            if (chapterSectionExperimentService.getSectionNumberbyExperiment(id) == 0) {
                if (experimentService.deleteExperiment(id))
                    return Response.success();
                return Response.error("删除失败");
            } else {
                return Response.error("删除失败,该实验与课程已绑定");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }
}
