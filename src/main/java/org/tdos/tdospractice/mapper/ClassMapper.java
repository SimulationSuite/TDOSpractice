package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.type.ClassNumber;
import org.tdos.tdospractice.body.ClassIdList;
import org.tdos.tdospractice.type.ClassStudents;

import java.util.List;

@Mapper
@Repository
public interface ClassMapper {
    List<ClassEntity> findAll();

    ClassEntity findClassById(String id);

    List<ClassNumber> findClassNumber();

    int insertClassByUser(ClassEntity classEntity);

    ClassEntity findClassByClassesId(String id);

    ClassEntity findClassByClasses(String name);

    int findClassCountByClassId(String classId);

    List<ClassStudents> findStudentsByClass(@Param("class_ids")List<String> classIds,@Param("name")String name,@Param("studentId")String studentId);
}
