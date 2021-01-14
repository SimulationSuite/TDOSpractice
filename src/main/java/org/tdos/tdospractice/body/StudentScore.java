package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

public class StudentScore {

    @JsonProperty("id")
    public String id;

    @JsonProperty("assignment_id")
    public String assignmentId;

    @JsonProperty("user_id")
    public String userId;

    @JsonProperty("score")
    public Integer score;

    @JsonProperty("status")
    public Integer status;

}
