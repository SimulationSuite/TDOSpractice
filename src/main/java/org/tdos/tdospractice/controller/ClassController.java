package org.tdos.tdospractice.controller;

import org.apache.tomcat.util.http.parser.TokenList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.mapper.ClassMapper;
import org.tdos.tdospractice.type.Response;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClassController {
    @Autowired
    private ClassMapper classMapper;

    @GetMapping(value = "/search_class")
    public Response<List<ClassEntity>> classList() {
        List<ClassEntity> classes = classMapper.findAll();
        return Response.success(classes.stream().filter(x -> !x.getId().equals("fb0a1080-b11e-427c-8567-56ca6105ea07")).collect(Collectors.toList()));
    }
}
