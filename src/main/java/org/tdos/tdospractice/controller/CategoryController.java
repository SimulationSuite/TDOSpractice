package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.entity.CategoryEntity;
import org.tdos.tdospractice.service.CategoryService;
import org.tdos.tdospractice.type.Response;

import java.util.Optional;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = "/insertCategory")
    public Response insertCategory(@RequestBody CategoryEntity categoryEntity) {
        if (categoryService.insert(categoryEntity) == 1)
            return Response.success();
        return Response.error("添加失败");
    }

    @GetMapping(value = "/findParentCategory")
    public Response findParentCategory() {
        return Response.success(categoryService.findParentCategory());
    }

    @GetMapping(value = "/findChildCategory")
    public Response findChildCategory(@RequestParam(value = "parent_category_id") String parent_category_id) {
        return Response.success(categoryService.findChildCategory(parent_category_id));
    }
}
