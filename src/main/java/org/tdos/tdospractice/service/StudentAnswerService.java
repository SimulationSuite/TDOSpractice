package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import org.tdos.tdospractice.body.StudentAnswer;
import org.tdos.tdospractice.entity.StudentAnswerEntity;
import org.tdos.tdospractice.type.StudentQuestionAnswer;

import java.util.List;
import java.util.Map;

public interface StudentAnswerService {

    PageInfo<StudentQuestionAnswer> getStudentAnswerByAssignmentUserId(String userId, String assignmentId, Integer perPage, Integer page);

    PageInfo<StudentAnswerEntity> getStudentAnswerByCourseId(String courseId, Integer perPage, Integer page);

    PageInfo<StudentAnswerEntity> getStudentAnswerByChapterId(String chapterId, Integer perPage, Integer page);

    PageInfo<StudentAnswerEntity> getStudentAnswerBySectionId(String sectionId, Integer perPage, Integer page);

    Map<String, Object> deleteStudentAnswerById(List<String> id);

    StudentAnswerEntity addStudentAnswer(StudentAnswer studentAnswer);

    Boolean modifyStudentAnswerById(StudentAnswer studentAnswer);

    List<StudentAnswerEntity> addStudentAnswerList(List<StudentAnswer> studentAnswerList);

    Boolean modifyStudentAnswerStatusById(StudentAnswer studentAnswer);

}
