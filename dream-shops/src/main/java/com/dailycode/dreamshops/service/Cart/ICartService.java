package com.dailycode.dreamshops.service.Cart;

import com.dailycode.dreamshops.model.Cart;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
}
