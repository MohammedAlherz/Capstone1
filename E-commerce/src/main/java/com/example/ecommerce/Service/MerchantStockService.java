package com.example.ecommerce.Service;

import com.example.ecommerce.Model.MerchantStock;
import com.example.ecommerce.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();
    private final MerchantService merchantService;
    private final ProductService productService;
    // 1. Get all merchant stocks

    public ArrayList<MerchantStock> getAllMerchantStocks() {
        return merchantStocks;
    }

    // 2. Add merchant stock

    public boolean addMerchantStock(MerchantStock merchantStock) {
        if(merchantService.getMerchantById(merchantStock.getMerchantId()) && productService.getProductById(merchantStock.getProductId())) {
            merchantStocks.add(merchantStock);
            return true;
        } else {
            return false; // Merchant not found
        }
    }

    // 3. Update merchant stock

    public Object updateMerchantStock(String id, MerchantStock merchantStock) {
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equals(id)) {
                if (!merchantService.getMerchantById(merchantStock.getMerchantId()) || !productService.getProductById(merchantStock.getProductId())) {
                    return "Merchant or Product not found";
                }
                merchantStocks.set(i, merchantStock);
                return true;
            }
        }
        return false; // Merchant stock not found
    }

    // 4. Delete merchant stock

    public boolean deleteMerchantStock(String id) {
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getId().equals(id)) {
                merchantStocks.remove(i);
                return true;
            }
        }
        return false;
    }
    //5. Get merchant stock by merchantId and productId
    public boolean addStock(String productId, String merchantId,int stock){
        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getProductId().equals(productId) && merchantStock.getMerchantId().equals(merchantId)) {
                merchantStock.setStock(merchantStock.getStock() + stock);
                return true;
            }
        }
        return false; // Merchant stock not found
    }

    // 6. Check if a merchant has a specific product
    public boolean merchantHasProduct(String merchantId, String productId) {
        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getMerchantId().equals(merchantId) && merchantStock.getProductId().equals(productId)) {
                return true; // Merchant has the product
            }
        }
        return false; // Merchant does not have the product
    }

    // 7. reduce stock
    public void reduceStock(String productId, String merchantId){
        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getProductId().equals(productId) && merchantStock.getMerchantId().equals(merchantId)) {
                if (merchantStock.getStock() > 0) {
                    merchantStock.setStock(merchantStock.getStock() - 1);
                }
            }
        }
    }

    // 8. Get stock by merchantId and productId
    public int getStockByMerchantAndProduct(String merchantId, String productId) {
        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getMerchantId().equals(merchantId) && merchantStock.getProductId().equals(productId)) {
                return merchantStock.getStock(); // Return the stock for the specific product and merchant
            }
        }
        return 0; // No stock found for the specified merchant and product
    }

    // 9. Get all products by merchantId
    public ArrayList<Product> getAllProductsByMerchant(String merchantId) {
        ArrayList<Product> productsByMerchant = new ArrayList<>();
        for (Product product : productService.getAllProducts()) {
            if (merchantHasProduct(merchantId, product.getId())) {
                productsByMerchant.add(product); // Add product if merchant has it
            }
        }
        if(productsByMerchant.isEmpty()) {
            return null; // No products found for the specified merchant
        }
        return productsByMerchant; // Return the list of products for the specified merchant
    }

    public String getMerchantByProduct(String productId) {
        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getProductId().equals(productId)) {
                return merchantStock.getMerchantId(); // Return the merchant ID for the specified product
            }
        }
        return null; // No merchant found for the specified product
    }

    public void increaseStock(String productId, String merchantId) {
        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getProductId().equals(productId) && merchantStock.getMerchantId().equals(merchantId)) {
                merchantStock.setStock(merchantStock.getStock() + 1);
                return; // Stock increased successfully
            }
        }
        // If no matching merchant stock found, you might want to handle it differently
        System.out.println("Merchant stock not found for product: " + productId + " and merchant: " + merchantId);
    }
}

