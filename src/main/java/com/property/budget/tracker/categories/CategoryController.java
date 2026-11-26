package com.property.budget.tracker.categories;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public Category create(@RequestParam String name, @RequestParam String type, @RequestParam String description) {
        return categoryService.create(name, type, description);
    }

    @PatchMapping("/rename")
    public Category renameCategory(@RequestParam String currentName, @RequestParam String newName) {
        return categoryService.renameCategory(currentName, newName);
    }

    @PutMapping("/update/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestParam String name, @RequestParam String type, @RequestParam String description, @RequestParam boolean isActive) {
        return categoryService.updateCategory(
                id, name, type, description, isActive);
    }

    @GetMapping("/type/{type}")
    public List<Category> getCategoriesByType(@PathVariable String type) {
        return categoryService.getCategoriesByType(type);
    }

    @GetMapping("/id/{id}")
    public Category findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @GetMapping("/name/{name}")
    public Category findByName(@PathVariable String name) {
        return categoryService.findByName(name);
    }

    @GetMapping
    public List<Category> getAllActiveCategories() {
        return categoryService.getAllActiveCategories();
    }
}
