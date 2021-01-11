package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.tdos.tdospractice.body.QuestionBack;
import org.tdos.tdospractice.entity.QuestionBackEntity;

import java.util.List;
import java.util.Map;

public interface QuestionBackService {

    Map<String, Object> deleteQuestionBackById(List<String> id);

    QuestionBackEntity addQuestionBack(QuestionBack questionBack);

    Boolean modifyQuestionBackById(QuestionBack questionBack);

}
