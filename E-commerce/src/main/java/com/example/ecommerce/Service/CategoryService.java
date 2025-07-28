package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Category;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {

    ArrayList<Category> categories = new ArrayList<>();

    // 1. Get all categories

    public ArrayList<Category> getAllCategories() {
        return categories;
    }

    // 2. Add category

    public void addCategory(Category category) {
        categories.add(category);
    }

    // 3. Update category

    public boolean updateCategory(String id, Category category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(id)) {
                categories.set(i, category);
                return true;
            }
        }
        return false;
    }

    // 4. Delete category

    public boolean deleteCategory(String id) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(id)) {
                categories.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean getCategoryById(String categoryId) {
        for (Category category : categories) {
            if (category.getId().equals(categoryId)) {
                return true;
            }
        }
        return false;
    }
}
