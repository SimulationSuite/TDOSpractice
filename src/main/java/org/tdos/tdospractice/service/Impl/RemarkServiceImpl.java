package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.tdos.tdospractice.body.DeleteRemark;
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
        if (ObjectUtils.isEmpty(remark.title)) {
            return new Pair<>(false, "title is not be null");
        }
        if (ObjectUtils.isEmpty(remark.content)) {
            return new Pair<>(false, "content is not be null");
        }
        if (ObjectUtils.isEmpty(remark.userId)) {
            return new Pair<>(false, "user_id is not be null");
        }
        if (ObjectUtils.isEmpty(remark.coursewareId)) {
            return new Pair<>(false, "courseware_id is not be null");
        }
        if (ObjectUtils.isEmpty(remark.type)) {
            return new Pair<>(false, "type is not be null");
        }
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
    public PageInfo<CoursewareRemark> getCoursewareRemarkList(String userId, String coursewareId, String title, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        List<CoursewareRemark> list = remarkMapper.getCoursewareRemarkList(userId, coursewareId, title);
        return new PageInfo<>(list);
    }

    @Override
    public Pair<Boolean, String> deleteRemark(DeleteRemark deleteRemark) {
        remarkMapper.deleteRemark(deleteRemark.remarkId);
        return new Pair<>(true,"");
    }


}
