package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Remark {

    @JsonProperty("courseware_id")
    public String coursewareId;

    @JsonProperty("user_id")
    public String userId;

    public String title;

    public String content;

    public Integer type;

    @JsonProperty("remark_page")
    public int remarkPage;

    @JsonProperty("remark_at")
    public long RemarkAt;

}
