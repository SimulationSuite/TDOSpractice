package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.StudentScoreEntity;

import java.util.List;

@Mapper
@Repository
public interface StudentScoreMapper {

    List<StudentScoreEntity> getStudentScoreBySectionId(@Param("sectionId") String sectionId);

    int deleteStudentScoreById(@Param("id") String id);

    boolean ifSectionStudentScoreById(String id);

    int modifyStudentScoreById(StudentScoreEntity studentScore);

    int addStudentScore(StudentScoreEntity studentScore);

}
