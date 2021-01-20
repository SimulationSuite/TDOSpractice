package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddSection {

    public String id;

    public String name;

    public int order;

    @JsonProperty("small_sections")
    public List<AddSmallSection> smallSections;

}
