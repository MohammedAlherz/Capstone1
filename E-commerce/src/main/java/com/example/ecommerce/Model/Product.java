package com.example.ecommerce.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    @NotEmpty(message = "ID cannot be empty")
    private String id;
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 4, message = "Name must be more than 3 characters")
    private String name;
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive number")
    private double price;
    @NotEmpty(message = "Category ID cannot be empty")
    private String categoryId;


    private int timesBought;
}
