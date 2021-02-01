package org.tdos.tdospractice.controller;

import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tdos.tdospractice.body.Remark;
import org.tdos.tdospractice.service.RemarkService;
import org.tdos.tdospractice.type.CoursewareRemark;
import org.tdos.tdospractice.type.Response;

@RestController
public class RemarkController {

    @Autowired
    private RemarkService remarkService;

    @PostMapping(value = "/upload_remark")
    public Response<String> uploadRemark(@RequestBody Remark remark) {
        Pair<Boolean, String> pair = remarkService.uploadRemark(remark);
        if (pair.getKey()) {
            return Response.success(null);
        }
        return Response.error(pair.getValue());
    }

    @GetMapping(value = "/get_courseware_remark_list")
    public Response<PageInfo<CoursewareRemark>> getCoursewareRemarkList(@RequestParam(value = "courseware_id") String coursewareId,
                                                                        @RequestParam(value = "user_id") String userId,
                                                                        @RequestParam(value = "per_page") Integer perPage,
                                                                        @RequestParam(value = "title", required = false) String title,
                                                                        @RequestParam(value = "page") Integer page) {
        return Response.success(remarkService.getCoursewareRemarkList(userId, coursewareId, title, perPage, page));
    }


}
