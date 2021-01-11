package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddCourse {

    public String name;

    @JsonProperty("pic_url")
    public String picUrl;

    public List<AddChapter> chapters;

    public String introduction;

    @JsonProperty("owner_id")
    public String ownerId;

}