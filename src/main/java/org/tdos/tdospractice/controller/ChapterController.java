package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.body.Chapter;
import org.tdos.tdospractice.service.ChapterService;
import org.tdos.tdospractice.type.Response;

@RestController
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @PostMapping(value = "/modify_chapter_name_by_id")
    public Response<String> modifyChapterNameById(@RequestBody Chapter chapter) {
        Pair<Boolean, String> pair = chapterService
                .modifyChapterNameById(chapter.chapterID, chapter.chapterName, chapter.introduction);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

}
