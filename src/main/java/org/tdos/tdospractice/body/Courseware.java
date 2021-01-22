package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.RequestParam;

public class Courseware {

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("type")
    public Integer type;

    @JsonProperty("kind")
    public Integer kind;

    @JsonProperty("url")
    public String url;

    @JsonProperty("duration")
    public Integer duration;

    @JsonProperty("size")
    public Integer size;

}
