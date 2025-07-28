package com.example.ecommerce.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {
    @NotEmpty(message = "ID cannot be empty")
    private String id;
    @NotEmpty(message = "Product ID cannot be empty")
    private String productId;
    @NotEmpty(message = "Merchant ID cannot be empty")
    private String merchantId;
    @NotNull(message = "Merchant Name cannot be empty")
    @Min(value = 11, message = "Stock must be more than 10")
    private int stock;
}
