package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.CategoryEntity;
import org.tdos.tdospractice.mapper.CategoryMapper;
import org.tdos.tdospractice.service.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public int insert(CategoryEntity categoryEntity) {
        return categoryMapper.insert(categoryEntity);
    }

    @Override
    public List<CategoryEntity> findParentCategory() {
        return categoryMapper.findParentCategory();
    }

    @Override
    public List<CategoryEntity> findChildCategory(String parent_category_id) {
        return categoryMapper.findChildCategory(parent_category_id);
    }

    @Override
    public Optional<CategoryEntity> findCategory(String id) {
        return Optional.ofNullable(categoryMapper.findCategory(id));
    }
}
