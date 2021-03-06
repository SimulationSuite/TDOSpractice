package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Chapter {

    public String id;

    public String name;

    public int order;

    public List<Section> sections;

    @JsonProperty("chapter_has_video")
    public Boolean chapterHasVideo;

    @JsonProperty("chapter_has_pdf")
    public Boolean chapterHasPdf;

}
