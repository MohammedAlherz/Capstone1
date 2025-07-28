package com.example.ecommerce.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Merchant {
    @NotEmpty(message = "ID cannot be empty")
    private String id;
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 4, message = "Username must be more than 3 characters")
    private String name;
}
