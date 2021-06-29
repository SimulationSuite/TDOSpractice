package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.CategoryEntity;
import org.tdos.tdospractice.type.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    int insert(CategoryEntity categoryEntity);

    List<CategoryEntity> findParentCategory();

    List<CategoryEntity> findChildCategory(String parent_category_id);

    Optional<CategoryEntity> findCategory(String id);

    List<CategoryEntity> findAllChildCategory();

    Optional<CategoryEntity> findCategoryByName(String name);

    int insertList(Category category);

    int updateCategory(CategoryEntity categoryEntity);
}
