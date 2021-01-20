package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionBackAssignment {

    @JsonProperty("assignment_id")
    public String assignmentId;

    @JsonProperty("question_id")
    public String questionId;

    @JsonProperty("score")
    public Integer score;

}
