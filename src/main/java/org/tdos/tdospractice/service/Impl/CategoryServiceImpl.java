package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.CategoryEntity;
import org.tdos.tdospractice.mapper.CategoryMapper;
import org.tdos.tdospractice.service.CategoryService;
import org.tdos.tdospractice.type.Category;

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

    @Override
    public List<CategoryEntity> findAllChildCategory() {
        return categoryMapper.findAllChildCategory();
    }

    @Override
    public Optional<CategoryEntity> findCategoryByName(String name) {
        return Optional.ofNullable(categoryMapper.findCategoryByName(name));
    }

    @Override
    public int insertList(Category category) {
        CategoryEntity parentCategory = category.parentCategory;
        int a = 0;
        int b = 0;
        if (parentCategory.getId() == ""){
            int result = categoryMapper.insert(parentCategory);
            a = a+result;
        }else {
            int result =categoryMapper.updateCategory(parentCategory.getId(),parentCategory.getName());
            a = a+result;
        }
        for (CategoryEntity categoryEntity : category.getCategories()){
            if (categoryEntity.getId() == ""){
                categoryEntity.setParent_category_id(parentCategory.getId());
                int result = categoryMapper.insert(categoryEntity);
                b = b+result;
            }else {
                int result = categoryMapper.updateCategory(categoryEntity.getId(),categoryEntity.getName());
                b = b+result;
            }
        }
        if (a+b == category.getCategories().size()+1){
            return 1;
        }
        return 0;
    }

    @Override
    public int updateCategory(CategoryEntity categoryEntity) {
        return categoryMapper.updateCategory(categoryEntity.getId(),categoryEntity.getName());
    }


}
