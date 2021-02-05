package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.ClassIdList;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.mapper.ClassMapper;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.type.ClassStudents;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassMapper classMapper;

    @Override
    public List<ClassEntity> findAll() {
        return classMapper.findAll();
    }

    @Override
    public ClassEntity findClassById(String id) {
        return classMapper.findClassById(id);
    }

    @Override
    public int findClassCountByClassId(String classId) {
        return classMapper.findClassCountByClassId(classId);
    }

    @Override
    public PageInfo<ClassStudents> findStudentsByClass(List<String> classIds, int page, int per_page) {
        List<ClassStudents> list;
        PageHelper.startPage(page,per_page);
        list = classMapper.findStudentsByClass(classIds);
        return new PageInfo<ClassStudents>(list);
    }
}
