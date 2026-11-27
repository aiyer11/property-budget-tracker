package com.property.budget.tracker.categories;

import java.util.List;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Category> create(@RequestParam String name, @RequestParam String type, @RequestParam(required = false) String description) {
        return ResponseEntity.ok(categoryService.create(name, type, description));
    }

    @PatchMapping("/rename")
    public ResponseEntity<Category> renameCategory(@RequestParam String currentName, @RequestParam String newName) {
        return ResponseEntity.ok(categoryService.renameCategory(currentName, newName));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestParam String name, @RequestParam String type, @RequestParam(required = false) String description, @RequestParam boolean isActive) {
        return ResponseEntity.ok(categoryService.updateCategory(
                id, name, type, description, isActive));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Category>> getCategoriesByType(@PathVariable String type) {
        return ResponseEntity.ok(categoryService.getCategoriesByType(type));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Category> findByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.findByName(name));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Category>> getAllActiveCategories() {
        return ResponseEntity.ok(categoryService.getAllActiveCategories());
    }

    @PostMapping("/deactivate/{id}")
    public void deactivateCategory(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
    }
}
