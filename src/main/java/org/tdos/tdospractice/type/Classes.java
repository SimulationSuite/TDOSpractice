package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Classes {

    @JsonProperty("class_id")
    public String classId;

    @JsonProperty("user_id_list")
    public List<String> userIds;

    public Boolean completed;

}
