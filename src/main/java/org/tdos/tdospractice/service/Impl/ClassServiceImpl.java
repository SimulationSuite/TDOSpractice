package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.body.ClassIdList;
import org.tdos.tdospractice.entity.ClassEntity;
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
    public List<ClassStudents> findStudentsByClass(List<String> classIds) {
        return classMapper.findStudentsByClass(classIds);
    }
}
