package com.property.budget.tracker.categories;

import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category renameCategory(String currentName, String newName) {
        Category category = categoryRepository.findByNameIgnoreCase(currentName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with name: " + currentName));
        category.setName(newName);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        category.setName(updatedCategory.getName());
        category.setType(updatedCategory.getType());
        category.setDescription(updatedCategory.getDescription());
        category.setActive(updatedCategory.isActive());
        return categoryRepository.save(category);
    }

    public List<Category> getCategoriesByType(String type) {
        return categoryRepository.findByType(type);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }

    public Category findByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with name: " + name));
    }

    public List<Category> getAllActiveCategories() {
        return categoryRepository.findAll().stream()
                .filter(Category::isActive)
                .toList();
    }
}
