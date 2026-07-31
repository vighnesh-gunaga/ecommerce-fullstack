package com.example.EcommarceWebsite.controller;

import com.example.EcommarceWebsite.dto.CartRequest;
import com.example.EcommarceWebsite.dto.CartResponse;
import com.example.EcommarceWebsite.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/mycart")
    public CartResponse getMyCart() {
        return cartService.getMyCart();
    }

    @PostMapping("/addtocart")
    public CartResponse addToCart(
            @Valid @RequestBody CartRequest request
    ) {
        return cartService.addToCart(request);
    }
    @PutMapping("/updatecart")
    public CartResponse updateCart(
            @Valid @RequestBody CartRequest request
    ) {
        return cartService.updateCart(request);
    }
    @DeleteMapping("/remove/{cartItemId}")
    public CartResponse removeCartItem(
            @PathVariable Long cartItemId
    ) {
        return cartService.removeCartItem(cartItemId);
    }
    @DeleteMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "Cart cleared successfully";
    }
}

