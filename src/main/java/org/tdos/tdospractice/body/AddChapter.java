package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddChapter {

    public String id;

    public String name;

    @JsonProperty("introduction")
    public String introduction;

    public int order;

    public List<AddSections> sections;

}
