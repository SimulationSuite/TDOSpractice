package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class StudentScoreList {

    @JsonProperty("student_score_list")
    public List<StudentScore> studentScoreList;

}
