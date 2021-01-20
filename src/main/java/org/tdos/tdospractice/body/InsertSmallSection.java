package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InsertSmallSection {

    @JsonProperty("small_section_name")
    public String smallSectionName;

    @JsonProperty("section_id")
    public String sectionId;

    @JsonProperty("chapter_id")
    public String chapterId;

    @JsonProperty("course_id")
    public String courseId;

}
