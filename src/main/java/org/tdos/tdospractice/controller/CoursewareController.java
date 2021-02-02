package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.entity.ChapterSectionCoursewareEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.CoursewareService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.body.CoursewareIdList;
import org.tdos.tdospractice.body.Courseware;
import org.tdos.tdospractice.body.ChapterSectionCourseware;
import org.tdos.tdospractice.body.ChapterSectionCoursewareList;
import java.util.*;

@RestController
public class CoursewareController {

    @Autowired
    private CoursewareService coursewareService;

    @GetMapping(value = "/getCoursewareAll")
    public Response<PageInfo<CoursewareEntity>> getCoursewareAll(@RequestParam(value = "name", required = false) String name,
                                                                 @RequestParam(value = "kind", required = false) Integer kind,
                                                                 @RequestParam(value = "type", required = false) Integer type,
                                                                 @RequestParam(value = "category_id", required = false) String categoryId,
                                                                 @RequestParam(value = "c_category_id", required = false) String cCategoryId,
                                                                 @RequestParam(value = "chapter_id", required = false) String chapterId,
                                                                 @RequestParam(value = "section_id", required = false) String sectionId,
                                                                 @RequestParam(value = "perPage") Integer perPage,
                                                                 @RequestParam(value = "page") Integer page) {
        return Response.success(coursewareService.getCoursewareAll(name, kind, type, categoryId, cCategoryId, chapterId, sectionId, perPage, page));
    }

    @GetMapping(value = "/getCoursewareByClassId")
    public Response<PageInfo<CoursewareEntity>> getCoursewareByClassId(@RequestParam(value = "classId") String classId,
                                                                       @RequestParam(value = "perPage") Integer perPage,
                                                                       @RequestParam(value = "page") Integer page) {
        return Response.success(coursewareService.getCoursewareByClassId(classId, perPage, page));
    }

    @GetMapping(value = "/getCoursewareBySectionId")
    public Response<PageInfo<CoursewareEntity>> getCoursewareBySectionId(@RequestParam(value = "sectionId") String sectionId,
                                                                         @RequestParam(value = "kind", required = false) Integer kind,
                                                                         @RequestParam(value = "type", required = false) Integer type,
                                                                         @RequestParam(value = "perPage") Integer perPage,
                                                                         @RequestParam(value = "page") Integer page) {
        return Response.success(coursewareService.getCoursewareBySectionId(sectionId, kind, type, perPage, page));
    }

    @GetMapping(value = "/getCoursewareByChapterId")
    public Response<PageInfo<CoursewareEntity>> getCoursewareByChapterId(@RequestParam(value = "chapterId") String chapterId,
                                                                         @RequestParam(value = "kind", required = false) Integer kind,
                                                                         @RequestParam(value = "type", required = false) Integer type,
                                                                         @RequestParam(value = "perPage") Integer perPage,
                                                                         @RequestParam(value = "page") Integer page) {
        return Response.success(coursewareService.getCoursewareByChapterId(chapterId, kind, type, perPage, page));
    }

    @GetMapping(value = "/getCoursewareByCourseId")
    public Response<PageInfo<CoursewareEntity>> getCoursewareByCourseId(@RequestParam(value = "courseId") String courseId,
                                                                        @RequestParam(value = "kind", required = false) Integer kind,
                                                                        @RequestParam(value = "type", required = false) Integer type,
                                                                        @RequestParam(value = "perPage") Integer perPage,
                                                                        @RequestParam(value = "page") Integer page) {
        return Response.success(coursewareService.getCoursewareByCourseId(courseId, kind, type, perPage, page));
    }

    @PostMapping(value = "/deleteCoursewareById")
    public Response<Map<String, Object>> deleteCoursewareById(@RequestBody CoursewareIdList idList) {
        Map<String, Object> map = coursewareService.deleteCoursewareById(idList.coursewareIdList);
        return Response.success(map);
    }

    @PostMapping(value = "/deleteChapterSectionCourseById")
    public Response<Map<String, Object>> deleteChapterSectionCourseById(@RequestBody ChapterSectionCoursewareList chapterSectionCoursewareList) {
        Map<String, Object> map = coursewareService.deleteChapterSectionCourseById(chapterSectionCoursewareList.chapterSectionCoursewareList);
        return Response.success(map);
    }

    @PostMapping(value = "/addCourseware")
    public Response<CoursewareEntity> addCourseware(@RequestBody Courseware courseware) {
        return Response.success(coursewareService.addCourseware(courseware));
    }

    @PostMapping(value = "/modifyCoursewareNameById")
    public Response<Map<String, Object>> modifyCoursewareNameById(@RequestBody Courseware courseware) {
        return Response.success(coursewareService.modifyCoursewareNameById(courseware));
    }

    @PostMapping(value = "/addChapterSectionCourseware")
    public Response<ChapterSectionCoursewareEntity> addChapterSectionCourseware(@RequestBody ChapterSectionCourseware chapterSectionCourseware) {
        return Response.success(coursewareService.addChapterSectionCourseware(chapterSectionCourseware));
    }

}

