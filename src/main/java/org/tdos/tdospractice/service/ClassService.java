package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import org.tdos.tdospractice.body.ClassIdList;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.type.ClassStudents;

import java.util.List;

public interface ClassService {
    List<ClassEntity> findAll();

    ClassEntity findClassById(String id);

    int findClassCountByClassId(String classId);

    PageInfo<ClassStudents> findStudentsByClass(List<String> classIds,String name,String studentId, int page, int per_page);
}
