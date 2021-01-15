package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.type.ClassNumber;

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
}
