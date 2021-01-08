package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InsertSection {

    @JsonProperty("section_name")
    public String sectionName;

    @JsonProperty("chapter_id")
    public String chapterId;

    @JsonProperty("course_id")
    public String courseId;

}
