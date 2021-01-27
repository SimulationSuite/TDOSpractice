package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CategoryMapper {
    int insert(CategoryEntity categoryEntity);

    List<CategoryEntity> findParentCategory();

    List<CategoryEntity> findChildCategory(String parent_category_id);

    CategoryEntity findCategory(String id);
}
