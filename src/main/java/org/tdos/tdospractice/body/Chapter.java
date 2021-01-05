package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Chapter {

    @JsonProperty("chapter_id")
    public String chapterID;

    @JsonProperty("chapter_name")
    public String chapterName;

    @JsonProperty("introduction")
    public String introduction;

}
