package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ModifyCourseStatus {

    @JsonProperty("owner_id")
    public String ownerId;

    @JsonProperty("course_id")
    public String courseId;

    public String start;

    public String end;

    @JsonProperty("user_id_list")
    public List<String> userIds;

    public Integer status;

}
