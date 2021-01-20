package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class QuestionBackAssignmentList {

    @JsonProperty("question_back_assignment_list")
    public List<QuestionBackAssignment> questionBackAssignmentList;

}
