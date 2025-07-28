package com.example.ecommerce.Service;

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
    HashMap<String, String>  purchasedCart = new HashMap<>();
    Map<String, Map<String, Double>> userRatings = new HashMap<>();

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
                                purchasedCart.put(userId,productId);
                                return true;
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

    // 7. check if user bought the product
    public Object rateProduct(String userId, String productId, double rating) {
        for (Map.Entry<String, String> entry : purchasedCart.entrySet()) {
            if (entry.getKey().equals(userId) && entry.getValue().equals(productId)) {
                if (rating > 0 && rating <= 5) {
                    // Store or update the user's rating
                    userRatings.putIfAbsent(productId, new HashMap<>());
                    userRatings.get(productId).put(userId, rating);

                    // Calculate average rating
                    double avgRating = userRatings.get(productId)
                            .values()
                            .stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0.0);

                    // Update product's average rating
                    productService.setRating(avgRating, productId);
                    return productService.getProductByIdObject(productId);
                } else {
                    return "Rating should be between 1 and 5";
                }
            }
        }
        return "User does not purchase this product / User or product does not exist";
    }

    // 8. Refund a product
    public Object refundProduct(String userId, String productId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                if (purchasedCart.containsKey(userId) && purchasedCart.get(userId).equals(productId)) {
                    // Refund the product price to the user's balance
                    user.setBalance(user.getBalance() + productService.getProductPrice(productId));
                    // Increase the stock of the merchant
                    String merchantId = merchantStockService.getMerchantByProduct(productId);
                    merchantStockService.increaseStock(productId, merchantId);
                    // Remove the product from user's purchased cart
                    purchasedCart.remove(userId);
                    // Remove the product from userProductMap
                    if (userProductMap.containsKey(userId)) {
                        userProductMap.get(userId).remove(productId);
                    }
                    return "Refund successful";
                } else {
                    return "User has not purchased this product";
                }
            }
        }
        return "User not found";
    }

}
