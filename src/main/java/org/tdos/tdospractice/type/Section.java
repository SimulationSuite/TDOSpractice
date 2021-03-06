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

    @JsonProperty("section_has_video")
    public Boolean sectionHasVideo;

    @JsonProperty("section_has_pdf")
    public Boolean sectionHasPdf;

    @JsonProperty("section_has_assignment")
    public Boolean sectionHasAssignment;

    @JsonProperty("section_has_experiment")
    public Boolean sectionHasExperiment;

}
