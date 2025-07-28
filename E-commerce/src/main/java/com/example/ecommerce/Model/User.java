package com.example.ecommerce.Model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    @NotEmpty(message = "ID cannot be empty")
    private String id;
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 6, message = "Username must to be more than 5 characters")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{7,}$", message = "Password must be at least 7 characters long and contain at least one letter and one number")
    private String password;
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;
    @NotEmpty(message = "Role cannot be empty")
    @Pattern(regexp = "^(Admin|Customer)$", message = "Role must be either Admin or Customer")
    private String role;
    @NotNull(message = "Balance cannot be empty")
    @Positive(message = "Balance must be a positive number")
    private double balance;


}
