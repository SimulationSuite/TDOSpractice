package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.tdos.tdospractice.body.StudentAnswer;
import org.tdos.tdospractice.entity.StudentAnswerEntity;

import java.util.List;
import java.util.Map;

public interface StudentAnswerService {

    List<StudentAnswerEntity> getStudentAnswerBySectionId(String sectionId);

    Map<String, Object> deleteStudentAnswerById(List<String> id);

    StudentAnswerEntity addStudentAnswer(StudentAnswer studentAnswer);

    Boolean modifyStudentAnswerById(StudentAnswer studentAnswer);

}
