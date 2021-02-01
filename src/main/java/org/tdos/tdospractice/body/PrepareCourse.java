package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PrepareCourse {

    @JsonProperty("course_id")
    public String courseId;

    @JsonProperty("user_id")
    public String userId;

}
