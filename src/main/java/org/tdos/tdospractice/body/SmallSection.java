package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SmallSection {

    @JsonProperty("small_section_id")
    public String sectionID;

    @JsonProperty("small_section_name")
    public String smallSectionName;

}
