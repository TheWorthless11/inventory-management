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
}