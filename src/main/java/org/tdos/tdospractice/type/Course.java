package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class Course {

    public String id;

    public String name;

    @JsonProperty("pic_url")
    public String picUrl;

    List<Chapter> chapters;

    public String introduction;

    @JsonProperty("start_at")
    public LocalDateTime startAt;

    public int status;

    @JsonProperty("owner_id")
    public String ownerId;

    public int type;

    @JsonProperty("model_id")
    public String modelId;

    @JsonProperty("created_at")
    public LocalDateTime createdAt;

    @JsonProperty("updated_at")
    public LocalDateTime updatedAt;

}
