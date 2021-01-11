package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModifyCourseStatus {

    @JsonProperty("user_id")
    public String userId;

    @JsonProperty("course_id")
    public String courseId;

}
