package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductService {

    ArrayList<Product> products = new ArrayList<>();
    private final CategoryService categoryService;
    // 1. Get all products

    public ArrayList<Product> getAllProducts() {
        return products;
    }
    // 2. Add product
    public boolean addProduct(Product product) {

        if (categoryService.getCategoryById(product.getCategoryId())) {
            products.add(product);
            return true;
        }
        return false;
    }

    // 3. Update product

    public Object updateProduct(String id, Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                if (!categoryService.getCategoryById(product.getCategoryId())) {
                    return "Category not found";
                }
                products.set(i, product);
                return true;
            }
        }
        return false;
    }

    // 4. Delete product

    public boolean deleteProduct(String id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    // 5. Get product by ID
    public boolean getProductById(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return true; // Product found
            }
        }
        return false; // Product not found
    }

    // 6. Get product price by ID
    public double getProductPrice(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product.getPrice(); // Return the price of the product
            }
        }
        return -1; // Product not found or no price set
    }

    // 7. Increment times bought
    public void incrementTimesBought(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setTimesBought(product.getTimesBought() + 1);
            }
        }
    }

    // 8. Get best-selling product
    public Product getBestSellingProduct() {
        if (products.isEmpty()) {
            return null; // No products available
        }
        Product bestSelling = products.get(0);
        for (Product product : products) {
            if (product.getTimesBought() > bestSelling.getTimesBought()) {
                bestSelling = product;
            }
        }
        return bestSelling;
    }

    // 9. Get product by ID as an object
    public Product getProductByIdObject(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product; // Return the product object
            }
        }
        return null; // Product not found
    }

    //10. set rating by user

    public void setRating(double rating,String productId){
        for(Product p:products){
            if(p.getId().equals(productId)){
                p.setRatingStar(rating);
            }
        }
    }

}
