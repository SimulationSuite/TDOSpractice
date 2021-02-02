package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChapterSectionCoursewareList {

    @JsonProperty("chapter_section_courseware_list")
    public List<ChapterSectionCourseware> chapterSectionCoursewareList;

}
