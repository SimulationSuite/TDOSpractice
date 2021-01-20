package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChapterSectionCourseware {

    @JsonProperty("relative_id")
    public String relativeId;

    @JsonProperty("courseware_id")
    public String coursewareId;

    @JsonProperty("type")
    public int type;

}
