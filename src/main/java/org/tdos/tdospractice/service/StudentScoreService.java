package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.tdos.tdospractice.body.StudentScore;
import org.tdos.tdospractice.entity.StudentScoreEntity;

import java.util.List;
import java.util.Map;

public interface StudentScoreService {

    List<StudentScoreEntity> getStudentScoreBySectionId(String sectionId);

    Map<String, Object> deleteStudentScoreById(List<String> id);

    StudentScoreEntity addStudentScore(StudentScore studentScore);

    Boolean modifyStudentScoreById(StudentScore studentScore);

}
