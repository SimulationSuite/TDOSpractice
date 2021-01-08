package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AssignmentIdList {

    @JsonProperty("assignment_id_list")
    public List<String> assignmentIdList;

}
