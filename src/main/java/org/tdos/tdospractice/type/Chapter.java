package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class Chapter {

    public String id;

    public String name;

    public String introduction;

    public int order;

    public List<Section> sections;

    @JsonProperty("created_at")
    public LocalDateTime createdAt;

    @JsonProperty("updated_at")
    public LocalDateTime updatedAt;

}
