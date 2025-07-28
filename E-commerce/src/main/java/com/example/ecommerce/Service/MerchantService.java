package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Merchant;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantService {

    ArrayList<Merchant> merchants = new ArrayList<>();

    // 1. Get all merchants

    public ArrayList<Merchant> getAllMerchants() {
        return merchants;
    }

    // 2. Add merchant

    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }

    // 3. Update merchant

    public boolean updateMerchant(String id, Merchant merchant) {
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equals(id)) {
                merchants.set(i, merchant);
                return true;
            }
        }
        return false;
    }

    // 4. Delete merchant

    public boolean deleteMerchant(String id) {
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equals(id)) {
                merchants.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean getMerchantById( String merchantId) {
        for (Merchant merchant : merchants) {
            if (merchant.getId().equals(merchantId)) {
                return true; // Merchant found
            }
        }
        return false; // Merchant not found
    }
}
