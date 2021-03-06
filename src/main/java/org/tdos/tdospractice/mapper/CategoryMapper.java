package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    CategoryEntity findCategory(@Param("id")String id);

    List<CategoryEntity> findAllChildCategory();

    CategoryEntity findCategoryByName(@Param("name")String name);

    int updateCategory(@Param("id")String id,@Param("name")String name);

}
