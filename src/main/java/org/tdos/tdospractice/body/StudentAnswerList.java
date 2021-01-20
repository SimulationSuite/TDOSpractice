package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class StudentAnswerList {

    @JsonProperty("student_answer_list")
    public List<StudentAnswer> studentAnswerList;

}
