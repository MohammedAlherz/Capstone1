package com.example.ecommerce.Controller;

import com.example.ecommerce.Api.ApiResponse;
import com.example.ecommerce.Model.Product;
import com.example.ecommerce.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 1. Get all products
    // Endpoint: GET /api/v1/products/get
    // Method: productService.getAllProducts()
    @GetMapping("/get")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.status(200).body(productService.getAllProducts());
    }

    // 2. Add product
    // Endpoint: POST /api/v1/products/add
    // Method: productService.addProduct(product)
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        boolean isAdded = productService.addProduct(product);
        if (isAdded) {
            return ResponseEntity.status(200).body(new ApiResponse("Product added successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
        }
    }

    // 3. Update product
    // Endpoint: PUT /api/v1/products/update/{id}
    // Method: productService.updateProduct(id, product)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        Object result = productService.updateProduct(id, product);
        if (result instanceof Boolean && (Boolean) result) {
            return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully"));
        } else if (result instanceof String) {
            return ResponseEntity.status(400).body(new ApiResponse((String) result));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
    }

    // 4. Delete product
    // Endpoint: DELETE /api/v1/products/delete/{id}
    // Method: productService.deleteProduct(id)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
    }

    // 1. Extra Endpoints:

    // 1.1 get the best-selling product
    // Endpoint: GET /api/v1/products/best-selling
    // Method: productService.getBestSellingProduct()
    @GetMapping("/best-selling")
    public ResponseEntity<?> getBestSellingProduct() {
        Product bestSellingProduct = productService.getBestSellingProduct();
        if (bestSellingProduct != null) {
            return ResponseEntity.status(200).body(bestSellingProduct);
        } else {
            return ResponseEntity.status(404).body(new ApiResponse("No products found"));
        }
    }




}
