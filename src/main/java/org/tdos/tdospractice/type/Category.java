package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.tdos.tdospractice.entity.CategoryEntity;

import java.util.List;

@Getter
public class Category {

    @JsonProperty("parent_category")
    public CategoryEntity parentCategory;

    @JsonProperty("categories")
    public List<CategoryEntity> categories;

}
