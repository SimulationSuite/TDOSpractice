package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class CoursewareRemark {

    @JsonProperty("remark_id")
    public String id;

    @JsonProperty("courseware_name")
    public String coursewareName;

    public String title;

    public String content;

    public int type;

    public String url;

    @JsonProperty("remark_page")
    public int remarkPage;

    @JsonProperty("remark_at")
    public long remarkAt;

    @JsonProperty("created_at")
    public LocalDateTime createdAt;

}
