package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.Remark;
import org.tdos.tdospractice.entity.CoursewareEntity;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.mapper.CoursewareMapper;
import org.tdos.tdospractice.mapper.RemarkMapper;
import org.tdos.tdospractice.mapper.UserMapper;
import org.tdos.tdospractice.service.RemarkService;
import org.tdos.tdospractice.type.CoursewareRemark;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RemarkServiceImpl implements RemarkService {

    @Autowired
    private RemarkMapper remarkMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CoursewareMapper coursewareMapper;


    @Override
    public Pair<Boolean, String> uploadRemark(Remark remark) {
        UserEntity userEntity = userMapper.findUserById(remark.userId);
        if (userEntity == null) {
            return new Pair<>(false, "user_id is not exist");
        }
        if (userEntity.getRoleID() != 2) {
            return new Pair<>(false, "role must be student");
        }
        if (coursewareMapper.hasSectionCoursewareId(remark.coursewareId) == 0) {
            return new Pair<>(false, "courseware_id is not exist");
        }
        remarkMapper.uploadRemark(remark);
        return new Pair<>(true, "");
    }

    @Override
    public PageInfo<CoursewareRemark> getCoursewareRemarkList(String userId, String sectionId,Integer perPage, Integer page) {
        List<String> courseIds = coursewareMapper.getCoursewareBySectionId(sectionId, null, null).stream()
                .map(CoursewareEntity::getId).collect(Collectors.toList());
        PageHelper.startPage(page, perPage);
        List<CoursewareRemark> list = remarkMapper.getCoursewareRemarkList(userId, courseIds);
        return new PageInfo<>(list);
    }


}
