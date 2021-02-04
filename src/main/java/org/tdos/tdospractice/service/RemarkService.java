package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.tdos.tdospractice.body.DeleteRemark;
import org.tdos.tdospractice.body.Remark;
import org.tdos.tdospractice.type.CoursewareRemark;

public interface RemarkService {

    Pair<Boolean, String> uploadRemark(Remark remark);

    PageInfo<CoursewareRemark> getCoursewareRemarkList(String userId, String courseId, String title, Integer perPage, Integer page);

    Pair<Boolean, String> deleteRemark(DeleteRemark deleteRemark);
}
