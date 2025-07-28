package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Product;
import com.example.ecommerce.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    ArrayList<User> users = new ArrayList<>();
    HashMap<String, HashMap<String, Integer>> userProductMap = new HashMap<>(); // userId -> (productId -> count)

    private final ProductService productService;
    private final MerchantService merchantService;
    private final MerchantStockService merchantStockService;
    //1. get all users

    public ArrayList<User> getAllUsers() {
        return users;
    }

    //2. add user

    public void addUser(User user) {
        users.add(user);
    }

    //3. update user

    public boolean updateUser(String id, User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.set(i, user);
                return true;
            }
        }
        return false;
    }

    //4. delete user

    public boolean deleteUser(String id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.remove(i);
                return true;
            }
        }
        return false;
    }

    //5. user buy product
    public Object buyProduct(String userId, String productId, String merchantId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                if (productService.getProductById(productId) && merchantService.getMerchantById(merchantId)) {
                    if (merchantStockService.merchantHasProduct(merchantId, productId)) {
                        if (merchantStockService.getStockByMerchantAndProduct(merchantId, productId) > 0) {
                            if (user.getBalance() >= productService.getProductPrice(productId)) {
                                // Deduct the product price from user's balance
                                user.setBalance(user.getBalance() - productService.getProductPrice(productId));
                                // Decrease the stock of the merchant
                                merchantStockService.reduceStock(productId, merchantId);
                                productService.incrementTimesBought(productId);
                                // Track the product bought by the user (FIXED)
                                userProductMap.putIfAbsent(userId, new HashMap<>());
                                HashMap<String, Integer> userProducts = userProductMap.get(userId);
                                userProducts.put(productId, userProducts.getOrDefault(productId, 0) + 1);
                                return "Purchase successful";
                            } else {
                                return "Insufficient balance";
                            }
                        } else {
                            return "Product is out of stock";
                        }
                    } else {
                        return "Merchant does not have the product";
                    }
                } else {
                    return "Product or Merchant not found";
                }
            }
        }
        return "User not found";
    }

    // 6. most purchased product by user
    public Object getMostPurchasedProduct(String userId) {
        // Check if user exists
        boolean userExists = users.stream().anyMatch(user -> user.getId().equals(userId));
        if (!userExists) {
            return "User not found"; // User does not exist
        }

        // Check if user has made any purchases
        if (!userProductMap.containsKey(userId) || userProductMap.get(userId).isEmpty()) {
            return "User hasn't bought any products";
        }

        HashMap<String, Integer> userProducts = userProductMap.get(userId);
        String mostPurchasedProductId = null;
        int maxCount = 0;

        // Find the product with the highest purchase count
        for (Map.Entry<String, Integer> entry : userProducts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostPurchasedProductId = entry.getKey();
            }
        }

        // Return the actual Product object
        if (mostPurchasedProductId != null) {
            return productService.getProductByIdObject(mostPurchasedProductId);
        }

        return null;
    }

}
