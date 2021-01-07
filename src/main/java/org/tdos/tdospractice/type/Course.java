package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Course {

    public String id;

    public String name;

    @JsonProperty("pic_url")
    public String picUrl;

    public List<Chapter> chapters;

    public String introduction;

    @JsonProperty("start_at")
    public LocalDateTime startAt;

    @JsonProperty("end_at")
    public LocalDateTime endAt;

    public int status;

    @JsonProperty("owner_id")
    public String ownerId;

    public int type;

    @JsonProperty("model_id")
    public String modelId;


}
