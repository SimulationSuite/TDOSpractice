package org.tdos.tdospractice.body;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DeleteIdList {

    @JsonProperty("delete_id_list")
    public List<String> deleteIdList;

}
