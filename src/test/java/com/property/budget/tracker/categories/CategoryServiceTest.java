package com.property.budget.tracker.categories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.property.budget.tracker.exceptions.CategoryNotFoundException;
import com.property.budget.tracker.exceptions.DuplicateCategoryException;
import com.property.budget.tracker.exceptions.InvalidCategoryException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void testCreate() {
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Category result = categoryService.create("  Utilities  ", "EXPENSE", "Utilities for the home");

        assertEquals("Utilities", result.getName());
        assertEquals("EXPENSE", result.getType());
        assertEquals("Utilities for the home", result.getDescription());
        verify(categoryRepository).save(result);
    }

    @Test
    void testCreateInvalidCategory(){
        assertThrows(InvalidCategoryException.class, () -> categoryService.create("  ", "EXPENSE", "Invalid category"));
        assertThrows(InvalidCategoryException.class, () -> categoryService.create(null, "EXPENSE", "Invalid category"));
        assertThrows(InvalidCategoryException.class, () -> categoryService.create("Valid Name", "  ", "Invalid category"));
        assertThrows(InvalidCategoryException.class, () -> categoryService.create("Valid Name", null, "Invalid category"));
    }

    @Test
    void testCreateDuplicateCategory() {
        when(categoryRepository.existsByNameIgnoreCase("Housing")).thenReturn(true);

        DuplicateCategoryException exception = assertThrows(DuplicateCategoryException.class,
                () -> categoryService.create("Housing", "EXPENSE", "Duplicate category"));

        assertEquals("Category already exists with name: Housing", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testRenameCategory() {
        Category category = new Category();
        category.setName("Housing");
        category.setType("EXPENSE");
        category.setDescription("Monthly costs");
        category.setActive(true);
        when(categoryRepository.findByNameIgnoreCase("Housing")).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.renameCategory("Housing", "  Home  ");

        assertSame(category, result);
        assertEquals("Home", result.getName());
        assertEquals("EXPENSE", result.getType());
        assertEquals("Monthly costs", result.getDescription());
        assertTrue(result.isActive());
        verify(categoryRepository).save(category);
    }

    @Test
    void testRenameCategoryNotFound() {
        when(categoryRepository.findByNameIgnoreCase("Missing")).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
                () -> categoryService.renameCategory("Missing", "New name"));

        assertEquals("Category not found with name: Missing", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testRenameCategoryDuplicate() {
        Category category = new Category();
        category.setName("Housing");
        category.setType("EXPENSE");
        category.setDescription("Monthly costs");
        category.setActive(true);
        when(categoryRepository.findByNameIgnoreCase("Housing")).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCaseAndIdNot("Home", category.getId())).thenReturn(true);

        DuplicateCategoryException exception = assertThrows(DuplicateCategoryException.class,
                () -> categoryService.renameCategory("Housing", "Home"));

        assertEquals("Category already exists with name: Home", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testUpdateCategory() {
        Category category = new Category();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.updateCategory(1L, "  Home  ", "EXPENSE", "Monthly costs", true);

        assertSame(category, result);
        assertEquals("Home", result.getName());
        assertEquals("EXPENSE", result.getType());
        assertEquals("Monthly costs", result.getDescription());
        assertTrue(result.isActive());
        verify(categoryRepository).save(category);
    }

    @Test
    void testUpdateCategoryDeactivate() {
        Category category = new Category();
        category.setActive(true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.updateCategory(1L, "Home", "EXPENSE", null, false);

        assertFalse(result.isActive());
    }

    @Test
    void testUpdateCategoryDuplicate() {
        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCaseAndIdNot("Home", 1L)).thenReturn(true);

        DuplicateCategoryException exception = assertThrows(DuplicateCategoryException.class,
                () -> categoryService.updateCategory(1L, "Home", "EXPENSE", null, true));

        assertEquals("Category already exists with name: Home", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testUpdateCategoryThrows() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategory(1L, "Home", "EXPENSE", null, true));

        assertEquals("Category not found with id: 1", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testGetCategoriesByType() {
        List<Category> categories = List.of(new Category());
        when(categoryRepository.findByType("EXPENSE")).thenReturn(categories);

        assertSame(categories, categoryService.getCategoriesByType("EXPENSE"));
        verify(categoryRepository).findByType("EXPENSE");
    }

    @Test
    void testFindById() {
        Category category = new Category();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertSame(category, categoryService.findById(1L));
    }

    @Test
    void testFindByIdThrows() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
                () -> categoryService.findById(1L));

        assertEquals("Category not found with id: 1", exception.getMessage());
    }

    @Test
    void testFindByName() {
        Category category = new Category();
        when(categoryRepository.findByNameIgnoreCase("Housing")).thenReturn(Optional.of(category));

        assertSame(category, categoryService.findByName("Housing"));
        verify(categoryRepository).findByNameIgnoreCase("Housing");
    }

    @Test
    void testFindByNameThrows() {
        when(categoryRepository.findByNameIgnoreCase("Missing")).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
                () -> categoryService.findByName("Missing"));

        assertEquals("Category not found with name: Missing", exception.getMessage());
    }

    @Test
    void testGetAllActiveCategories() {
        List<Category> categories = List.of(new Category());
        when(categoryRepository.findByIsActiveTrue()).thenReturn(categories);

        assertSame(categories, categoryService.getAllActiveCategories());
        verify(categoryRepository).findByIsActiveTrue();
    }

    @Test
    void testDeactivateCategory() {
        Category category = new Category();
        category.setActive(true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        categoryService.deactivateCategory(1L);

        assertFalse(category.isActive());
        verify(categoryRepository).save(category);
    }
}
