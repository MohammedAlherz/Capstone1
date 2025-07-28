package com.example.ecommerce.Controller;

import com.example.ecommerce.Api.ApiResponse;
import com.example.ecommerce.Model.Merchant;
import com.example.ecommerce.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    // 1. Get all merchants
    // Endpoint: GET /api/v1/merchants/get
    // Method: merchantService.getAllMerchants()
    @GetMapping("/get")
    public ResponseEntity<?> getAllMerchants() {
        return ResponseEntity.status(200).body(merchantService.getAllMerchants());
    }

    // 2. Add merchant
    // Endpoint: POST /api/v1/merchants/add
    // Method: merchantService.addMerchant(merchant)
    @PostMapping("/add")
    public ResponseEntity<?> addMerchant(@Valid @RequestBody Merchant merchant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        merchantService.addMerchant(merchant);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant added successfully"));
    }


    // 3. Update merchant
    // Endpoint: PUT /api/v1/merchants/update/{id}
    // Method: merchantService.updateMerchant(id, merchant)

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMerchant(@PathVariable String id, @Valid @RequestBody Merchant merchant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        boolean isUpdated = merchantService.updateMerchant(id, merchant);
        if (isUpdated) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant updated successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
        }
    }

    // 4. Delete merchant
    // Endpoint: DELETE /api/v1/merchants/delete/{id}
    // Method: merchantService.deleteMerchant(id)

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchant(@PathVariable String id) {
        boolean isDeleted = merchantService.deleteMerchant(id);
        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully"));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant not found"));
        }
    }
}
