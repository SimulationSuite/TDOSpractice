package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.StudentAnswerEntity;

import java.util.List;

@Mapper
@Repository
public interface StudentAnswerMapper {

    List<StudentAnswerEntity> getStudentAnswerByCourseId(@Param("courseId") String courseId);

    List<StudentAnswerEntity> getStudentAnswerByChapterId(@Param("chapterId") String chapterId);

    List<StudentAnswerEntity> getStudentAnswerBySectionId(@Param("sectionId") String sectionId);

    int deleteStudentAnswerById(@Param("id") String id);

    boolean ifSectionStudentAnswerById(String id);

    int modifyStudentAnswerById(StudentAnswerEntity studentAnswer);

    int addStudentAnswer(StudentAnswerEntity studentAnswer);

}
