package com.example.EcommarceWebsite.repository;

import com.example.EcommarceWebsite.model.Cart;
import com.example.EcommarceWebsite.model.CartItem;
import com.example.EcommarceWebsite.model.Product;
import com.example.EcommarceWebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUser(User user);

}
