package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddCourseCompleted {

    public List<AddChapter> chapters;

    @JsonProperty("course_id")
    public String courseId;

}