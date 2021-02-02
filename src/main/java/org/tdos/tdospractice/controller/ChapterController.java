package org.tdos.tdospractice.controller;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.Chapter;
import org.tdos.tdospractice.body.DeleteChapter;
import org.tdos.tdospractice.body.DeleteSection;
import org.tdos.tdospractice.body.InsertChapter;
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

    @PostMapping(value = "/add_chapter")
    public Response<String> addChapter(@RequestBody InsertChapter insertChapter) {
        Pair<Boolean, String> pair = chapterService.addChapter(insertChapter);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    @DeleteMapping(value = "/remove_chapter")
    public Response<String> removeChapter(@RequestBody DeleteChapter deleteChapter) {
        Pair<Boolean, String> pair = chapterService.removeChapter(deleteChapter);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    @GetMapping(value = "/get_chapter_by_id")
    public Response<Object> getChapter(@RequestParam(value = "chapter_id") String chapterId) {
        Pair<Boolean, Object> pair = chapterService.getChapter(chapterId);
        if (pair.getKey()) {
            return Response.success(pair.getValue());
        }
        return Response.error((String) pair.getValue());
    }

}
