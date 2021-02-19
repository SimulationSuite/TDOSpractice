package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrepareCourseReturn {

    public boolean exist;

    @JsonProperty("course_id")
    public String courseId;

    @JsonIgnore
    public String errMessage;

}
