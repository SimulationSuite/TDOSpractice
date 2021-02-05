package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClassIdList {
    @JsonProperty("class_id_list")
    public List<String> classIds;
}
