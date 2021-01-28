package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Section {

    public String id;

    public String name;

    public int order;

    @JsonProperty("small_sections")
    public List<SmallSection> smallSections;

}
