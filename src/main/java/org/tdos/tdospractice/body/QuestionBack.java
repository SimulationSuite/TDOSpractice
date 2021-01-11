package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class QuestionBack {

    @JsonProperty("id")
    public String id;

    @JsonProperty("type")
    public int type;

    @JsonProperty("answer")
    public String answer;

    @JsonProperty("question")
    public String question;

    @JsonProperty("model_id")
    public String modelId;

    @JsonProperty("category_id")
    public String categoryId;

}
