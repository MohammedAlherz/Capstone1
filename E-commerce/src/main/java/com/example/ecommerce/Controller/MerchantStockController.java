package com.example.ecommerce.Controller;

import com.example.ecommerce.Api.ApiResponse;
import com.example.ecommerce.Model.MerchantStock;
import com.example.ecommerce.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    // 1. Get all merchant stocks
    // Endpoint: GET /api/v1/merchant-stock/get
    // Method: merchantStockService.getAllMerchantStocks()
    @GetMapping("/get")
    public ResponseEntity<?> getAllMerchantStocks() {
        return ResponseEntity.status(200).body(merchantStockService.getAllMerchantStocks());
    }

    // 2. Add merchant stock
    // Endpoint: POST /api/v1/merchant-stock/add
    // Method: merchantStockService.addMerchantStock(merchantStock)
    @PostMapping("/add")
    public ResponseEntity<?> addMerchantStock(@Valid @RequestBody MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        boolean isAdded = merchantStockService.addMerchantStock(merchantStock);
        if (isAdded) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant stock added successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant or Product not found"));
        }
    }

    // 3. Update merchant stock
    // Endpoint: PUT /api/v1/merchant-stock/update/{id}
    // Method: merchantStockService.updateMerchantStock(id, merchantStock)

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMerchantStock(@PathVariable String id, @Valid @RequestBody MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        Object result = merchantStockService.updateMerchantStock(id, merchantStock);
        if (result instanceof Boolean && (Boolean) result) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant stock updated successfully"));
        } else if (result instanceof String) {
            return ResponseEntity.status(400).body(new ApiResponse((String) result));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
        }
    }

    // 4. Delete merchant stock
    // Endpoint: DELETE /api/v1/merchant-stock/delete/{id}
    // Method: merchantStockService.deleteMerchantStock(id)

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable String id) {
        boolean isDeleted = merchantStockService.deleteMerchantStock(id);
        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant stock deleted successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
        }
    }

    // 5. Add stock to merchant stock
    // Endpoint: PUT /api/v1/merchant-stock/add-stock/{merchantStockId}/{stock}
    // Method : merchantStockService.addStock(id,stock)
    @PutMapping("/add-stock/{productId}/{merchantID}/{stock}")
    public ResponseEntity<?> addStock(@PathVariable String productId,@PathVariable String merchantID, @PathVariable int stock) {
        boolean isStockAdded = merchantStockService.addStock(productId,merchantID, stock);
        if (isStockAdded) {
            return ResponseEntity.status(200).body(new ApiResponse("Stock added successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Product or Merchant not found"));
        }
    }

    // 1. Extra Endpoints:

    // 1.2 get products by same merchant
    // Endpoint: GET /api/v1/merchant-stock/get-by-merchant/{merchantId}
    // Method: merchantStockService.getProductsByMerchant(merchantId)

    @GetMapping("/get-by-merchant/{merchantId}")
    public ResponseEntity<?> getProductsByMerchant(@PathVariable String merchantId) {
        if(merchantStockService.getAllProductsByMerchant(merchantId) != null) {
            return ResponseEntity.status(200).body(merchantStockService.getAllProductsByMerchant(merchantId));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant not found or merchant has no products"));
        }
    }



}
