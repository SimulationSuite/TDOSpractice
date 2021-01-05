package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Section {

    @JsonProperty("section_id")
    public String sectionID;

    @JsonProperty("section_name")
    public String sectionName;

}
