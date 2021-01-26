package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddChapterCompleted {

    public AddChapter chapter;

    @JsonProperty("course_id")
    public String courseId;

}