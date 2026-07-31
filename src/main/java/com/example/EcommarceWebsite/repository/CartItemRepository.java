package com.example.EcommarceWebsite.repository;

import com.example.EcommarceWebsite.model.Cart;
import com.example.EcommarceWebsite.model.CartItem;
import com.example.EcommarceWebsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    void deleteAllByCart(Cart cart);
}
