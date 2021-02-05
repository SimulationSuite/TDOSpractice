package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DeleteRemark {

    @JsonProperty("remark_id_list")
    public List<String> remarkIdList;

}
