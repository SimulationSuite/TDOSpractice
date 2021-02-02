package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModifyExpiredCourseStatus {

    @JsonProperty("owner_id")
    public String ownerId;

    @JsonProperty("course_id")
    public String courseId;

}
