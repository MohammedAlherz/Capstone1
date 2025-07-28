package com.example.ecommerce.Controller;

import com.example.ecommerce.Api.ApiResponse;
import com.example.ecommerce.Model.Category;
import com.example.ecommerce.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 1. Get all categories
    // Endpoint: GET /api/v1/categories/get
    // Method: categoryService.getAllCategories()
    @GetMapping("/get")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.status(200).body(categoryService.getAllCategories());
    }

    // 2. Add category
    // Endpoint: POST /api/v1/categories/add
    // Method: categoryService.addCategory(category)
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@Valid @RequestBody Category category, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        categoryService.addCategory(category);
        return ResponseEntity.status(200).body(new ApiResponse("Category added successfully"));
    }

    // 3. Update category
    // Endpoint: PUT /api/v1/categories/update/{id}
    // Method: categoryService.updateCategory(id, category)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @Valid @RequestBody Category category, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        boolean isUpdated = categoryService.updateCategory(id, category);
        if (isUpdated) {
            return ResponseEntity.status(200).body(new ApiResponse("Category updated successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
        }
    }

    // 4. Delete category
    // Endpoint: DELETE /api/v1/categories/delete/{id}
    // Method: categoryService.deleteCategory(id)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        boolean isDeleted = categoryService.deleteCategory(id);
        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
        }
    }

}
