package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.body.DeleteSmallSection;
import org.tdos.tdospractice.body.InsertSmallSection;
import org.tdos.tdospractice.body.SmallSection;
import org.tdos.tdospractice.service.SmallSectionService;
import org.tdos.tdospractice.type.Response;

@RestController
public class SmallSectionController {

    @Autowired
    private SmallSectionService smallSectionService;

    @PostMapping(value = "/modify_small_section_name_by_id")
    public Response<String> modifySmallSectionNameById(@RequestBody SmallSection smallSection) {
        Pair<Boolean, String> pair = smallSectionService
                .modifySmallSectionNameById(smallSection.smallSectionID, smallSection.smallSectionName);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    @PostMapping(value = "/add_small_section")
    public Response<String> addSmallSection(@RequestBody InsertSmallSection insertSmallSection) {
        Pair<Boolean, String> pair = smallSectionService.addSmallSection(insertSmallSection);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    @DeleteMapping(value = "/remove_small_section")
    public Response<String> removeSmallSection(@RequestBody DeleteSmallSection deleteSmallSection) {
        Pair<Boolean, String> pair = smallSectionService.removeSmallSection(deleteSmallSection);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

}
