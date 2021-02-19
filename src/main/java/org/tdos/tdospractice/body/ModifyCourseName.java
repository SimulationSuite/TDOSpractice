package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModifyCourseName {

    @JsonProperty("course_id")
    public String courseId;

    @JsonProperty("owner_id")
    public String ownerId;

    @JsonProperty("course_name")
    public String courseName;

}
