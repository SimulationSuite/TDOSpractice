package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.body.DeleteSection;
import org.tdos.tdospractice.body.DeleteSmallSection;
import org.tdos.tdospractice.body.InsertSection;
import org.tdos.tdospractice.body.Section;
import org.tdos.tdospractice.service.SectionService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.utils.UUIDPattern;

@RestController
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @PostMapping(value = "/modify_section_name_by_id")
    public Response<String> modifySectionNameById(@RequestBody Section section) {
        Pair<Boolean, String> pair = sectionService
                .modifySectionNameById(section.sectionID, section.sectionName);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    @PostMapping(value = "/add_section")
    public Response<String> addSection(@RequestBody InsertSection insertSection) {
        Pair<Boolean, String> pair = sectionService.addSection(insertSection);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    @DeleteMapping(value = "/remove_section")
    public Response<String> removeSection(@RequestBody DeleteSection deleteSection) {
        Pair<Boolean, String> pair = sectionService.removeSection(deleteSection);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

}
