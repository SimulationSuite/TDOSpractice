package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.body.Chapter;
import org.tdos.tdospractice.body.Section;
import org.tdos.tdospractice.service.ChapterService;
import org.tdos.tdospractice.service.SectionService;
import org.tdos.tdospractice.type.Response;

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

}