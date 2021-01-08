package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InsertChapter {

    @JsonProperty("chapter_name")
    public String chapterName;

    @JsonProperty("introduction")
    public String introduction;

    @JsonProperty("course_id")
    public String courseId;

}
