package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.body.Remark;
import org.tdos.tdospractice.type.CoursewareRemark;

import java.util.List;

@Mapper
@Repository
public interface RemarkMapper {

    int uploadRemark(@Param("remark") Remark remark);

    List<CoursewareRemark> getCoursewareRemarkList(String userId, @Param("courseId") String courseId, String title);

    int deleteRemark(@Param("remarkIdList") List<String> remarkIdList);
}
