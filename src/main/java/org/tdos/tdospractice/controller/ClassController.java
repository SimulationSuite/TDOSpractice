package org.tdos.tdospractice.controller;

import com.github.pagehelper.PageInfo;
import org.apache.poi.hpsf.ClassID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.mapper.ClassMapper;
import org.tdos.tdospractice.body.ClassIdList;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.type.ClassStudents;
import org.tdos.tdospractice.type.Response;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClassController {
    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private ClassService classService;

    @GetMapping(value = "/search_class")
    public Response<List<ClassEntity>> classList() {
        List<ClassEntity> classes = classService.findAll();
        return Response.success(classes.stream().filter(x -> !x.getId().equals("fb0a1080-b11e-427c-8567-56ca6105ea07")).collect(Collectors.toList()));
    }

    @GetMapping(value = "/getClassCountByClassId")
    public Response<Integer> classList(@RequestParam(value = "classId")String classId) {
        int count = classService.findClassCountByClassId(classId);
        return Response.success(count);
    }

    @GetMapping(value = "/get_students_by_classes")
    public Response<PageInfo<ClassStudents>> getStudentsByClasses(@RequestParam(value = "classIds") List<String> classIds, @RequestParam(value = "per_page") int perPage, @RequestParam(value = "page") int page) {
        PageInfo<ClassStudents> list = classService.findStudentsByClass(classIds,page,perPage);
        return Response.success(list);
    }
}
