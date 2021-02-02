package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteCourse {

    @JsonProperty("course_id")
    public String courseId;

    @JsonProperty("owner_id")
    public String ownerId;

}
