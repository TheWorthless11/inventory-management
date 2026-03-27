package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAllCategories_returnsRepositoryData() {
        Category c1 = new Category(1L, "Electronics", null);
        Category c2 = new Category(2L, "Books", null);
        when(categoryRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Books", result.get(1).getName());
    }

    @Test
    void createCategory_whenNameBlank_throwsIllegalArgumentException() {
        Category category = new Category();
        category.setName("   ");

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(category));
    }

    @Test
    void createCategory_whenValid_savesCategory() {
        Category category = new Category();
        category.setName("Office");

        Category saved = new Category(10L, "Office", null);
        when(categoryRepository.save(category)).thenReturn(saved);

        Category result = categoryService.createCategory(category);

        assertEquals(10L, result.getId());
        assertEquals("Office", result.getName());
    }

    @Test
    void getCategoryById_whenFound_returnsCategory() {
        Category category = new Category(3L, "Hardware", null);
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(3L);

        assertEquals("Hardware", result.getName());
    }

    @Test
    void getCategoryById_whenNotFound_throwsResourceNotFoundException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void deleteCategory_whenExists_deletesById() {
        when(categoryRepository.existsById(5L)).thenReturn(true);

        categoryService.deleteCategory(5L);

        verify(categoryRepository).deleteById(5L);
    }

    @Test
    void deleteCategory_whenMissing_throwsResourceNotFoundException() {
        when(categoryRepository.existsById(6L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(6L));
    }

    @Test
    void updateCategory_whenMissing_throwsResourceNotFoundException() {
        Category update = new Category();
        update.setName("NewName");
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(99L, update));
    }

    @Test
    void updateCategory_whenNameBlank_throwsIllegalArgumentException() {
        Category update = new Category();
        update.setName("   ");

        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(1L, update));
    }

    @Test
    void updateCategory_whenValid_updatesAndSaves() {
        Category existing = new Category(1L, "Old", null);
        Category update = new Category();
        update.setName("  NewName  ");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = categoryService.updateCategory(1L, update);

        assertEquals("NewName", result.getName());
        verify(categoryRepository).save(argThat(c -> c.getId().equals(1L) && c.getName().equals("NewName")));
    }
}