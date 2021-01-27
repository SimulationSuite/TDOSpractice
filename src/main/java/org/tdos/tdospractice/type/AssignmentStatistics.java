package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AssignmentStatistics {

    public int total;

    public int committed;

    public int uncommitted;

    @JsonProperty("uncommitted_list")
    public List uncommittedList;

}
