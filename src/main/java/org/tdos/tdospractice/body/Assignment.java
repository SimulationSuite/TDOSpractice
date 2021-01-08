package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Assignment {

    @JsonProperty("id")
    public String id;

    @JsonProperty("section_id")
    public String sectionId;

    @JsonProperty("name")
    public String name;

    @JsonProperty("end_at")
    public LocalDateTime endAt;

    @JsonProperty("qualified_score")
    public Integer qualifiedScore;

}
