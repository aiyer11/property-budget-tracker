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
        category.setName(requireText(name, "name"));
        category.setType(requireText(type, "type"));
        category.setDescription(description);
        category.setActive(true);
        return categoryRepository.save(category);
    }

    public Category renameCategory(String currentName, String newName) {
        Category category = categoryRepository.findByNameIgnoreCase(currentName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with name: " + currentName));
        category.setName(requireText(newName, "name"));
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, String name, String type, String description, boolean isActive) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        category.setName(requireText(name, "name"));
        category.setType(requireText(type, "type"));
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

    public void deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        category.setActive(false);
        categoryRepository.save(category);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
