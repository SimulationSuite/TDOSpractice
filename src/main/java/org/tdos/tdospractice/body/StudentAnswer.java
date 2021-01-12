package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.RequestParam;

public class StudentAnswer {

    @JsonProperty("id")
    public String id;

    @JsonProperty("question_id")
    public String questionId;

    @JsonProperty("assignment_id")
    public String assignmentId;

    @JsonProperty("user_id")
    public String userId;

    @JsonProperty("answer")
    public String answer;

    @JsonProperty("score")
    public Integer score;

}
