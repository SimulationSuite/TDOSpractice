package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChapterSectionCourseware {

    @JsonProperty("chapter_id")
    public String chapterId;

    @JsonProperty("section_id")
    public String sectionId;

    @JsonProperty("courseware_id")
    public String coursewareId;

}
