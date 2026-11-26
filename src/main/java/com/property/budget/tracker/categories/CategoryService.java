package com.property.budget.tracker.categories;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(String name, String type, String description) {
        Category category = new Category();
        category.setName(name.trim());
        category.setType(type);
        category.setDescription(description);
        return categoryRepository.save(category);
    }

    public Category renameCategory(String currentName, String newName) {
        Category category = categoryRepository.findByNameIgnoreCase(currentName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with name: " + currentName));
        category.setName(newName.trim());
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, String name, String type, String description, boolean isActive) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        category.setName(name.trim());
        category.setType(type);
        category.setDescription(description);
        category.setActive(isActive);
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
        return categoryRepository.findByIsActiveTrue();
    }
}
