package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CoursewareIdList {

    @JsonProperty("courseware_id_list")
    public List<String> coursewareIdList;

}
