package com.property.budget.tracker.categories;

import com.property.budget.tracker.exceptions.InvalidCategoryException;
import com.property.budget.tracker.exceptions.CategoryNotFoundException;
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
                .orElseThrow(() -> new CategoryNotFoundException(currentName));
        category.setName(requireText(newName, "name"));
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, String name, String type, String description, boolean isActive) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        category.setName(requireText(name, "name"));
        category.setType(requireText(type, "type"));
        category.setDescription(description);
        category.setActive(isActive);
        return categoryRepository.save(category);
    }

    public List<Category> getCategoriesByType(String type) {
        return categoryRepository.findByType(requireText(type, "type"));
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Category findByName(String name) {
        return categoryRepository.findByNameIgnoreCase(requireText(name, "name"))
                .orElseThrow(() -> new CategoryNotFoundException(name));
    }

    public List<Category> getAllActiveCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    public void deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        category.setActive(false);
        categoryRepository.save(category);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidCategoryException(field + " must not be blank");
        }
        return value.trim();
    }
}
