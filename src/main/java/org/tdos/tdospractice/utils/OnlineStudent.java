package org.tdos.tdospractice.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class OnlineStudent {

    @JsonProperty("user_id")
    public String userID;

    public String name;

    @JsonProperty("class_name")
    public String className;

    @JsonProperty("grade_name")
    public String gradeName;

    public int type;  // 0 管理员 1 老师 2 学生

}
