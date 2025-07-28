package com.example.ecommerce.Controller;

import com.example.ecommerce.Api.ApiResponse;
import com.example.ecommerce.Model.User;
import com.example.ecommerce.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 1. Get all users
    // Endpoint: GET /api/v1/users/get
    // Method: userService.getAllUsers()
    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    // 2. Add user
    // Endpoint: POST /api/v1/users/add
    // Method: userService.addUser(user)
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user, Errors errors){
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }
    // 3. Update user
    // Endpoint: PUT /api/v1/users/update/{id}
    // Method: userService.updateUser(id, user)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        boolean isUpdated = userService.updateUser(id, user);
        if (isUpdated) {
            return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }
    }

    // 4. Delete user
    // Endpoint: DELETE /api/v1/users/delete/{id}
    // Method: userService.deleteUser(id)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }
    }

    // 5. User buy product
    // Endpoint: POST /api/v1/users/buy/{userId}/{productId}/{merchantId}
    // Method: userService.buyProduct(userId, productId, merchantId)
    @PostMapping("/buy/{userId}/{productId}/{merchantId}")
    public ResponseEntity<?> buyProduct(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        Object response = userService.buyProduct(userId, productId, merchantId);
        if (response instanceof String) {
            return ResponseEntity.status(400).body(new ApiResponse((String) response));
        } else {
            return ResponseEntity.status(200).body(new ApiResponse("Purchase successful"));
        }
    }

    // 1. Extra Endpoints:

    // 1.2 get highest product bought by user
    // Endpoint: GET /api/v1/users/highest-product-bought/{userId}
    // Method: userService.getMostPurchasedProduct(userId)

    @GetMapping("/highest-product-bought/{userId}")
    public ResponseEntity<?> getMostPurchasedProduct(@PathVariable String userId) {
        Object response = userService.getMostPurchasedProduct(userId);
        if (response instanceof String) {
            return ResponseEntity.status(400).body(new ApiResponse((String) response));
        } else {
            return ResponseEntity.status(200).body(response);
        }
    }

}
