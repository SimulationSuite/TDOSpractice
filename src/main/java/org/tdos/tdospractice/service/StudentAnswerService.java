package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import org.tdos.tdospractice.body.StudentAnswer;
import org.tdos.tdospractice.entity.StudentAnswerEntity;

import java.util.List;
import java.util.Map;

public interface StudentAnswerService {

    PageInfo<StudentAnswerEntity> getStudentAnswerByCourseId(String courseId, Integer perPage, Integer page);

    PageInfo<StudentAnswerEntity> getStudentAnswerByChapterId(String chapterId, Integer perPage, Integer page);

    PageInfo<StudentAnswerEntity> getStudentAnswerBySectionId(String sectionId, Integer perPage, Integer page);

    Map<String, Object> deleteStudentAnswerById(List<String> id);

    StudentAnswerEntity addStudentAnswer(StudentAnswer studentAnswer);

    Boolean modifyStudentAnswerById(StudentAnswer studentAnswer);

}
