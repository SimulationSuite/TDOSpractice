package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SectionCourseware {

    @JsonProperty("section_id")
    public String sectionId;

    @JsonProperty("courseware_id")
    public String coursewareId;

}
