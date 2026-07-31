package com.example.EcommarceWebsite.service;

import com.example.EcommarceWebsite.dto.CartItemResponse;
import com.example.EcommarceWebsite.dto.CartRequest;
import com.example.EcommarceWebsite.dto.CartResponse;
import com.example.EcommarceWebsite.exception.ProductNotFoundException;
import com.example.EcommarceWebsite.model.Cart;
import com.example.EcommarceWebsite.model.CartItem;
import com.example.EcommarceWebsite.model.Product;
import com.example.EcommarceWebsite.model.User;
import com.example.EcommarceWebsite.repository.CartItemRepository;
import com.example.EcommarceWebsite.repository.CartRepository;
import com.example.EcommarceWebsite.repository.ProductRepository;
import com.example.EcommarceWebsite.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public CartResponse getMyCart() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        return convertToCartResponse(cart);
    }

    @Transactional
    public CartResponse addToCart(CartRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product with id "
                                        + request.getProductId()
                                        + " not found"
                        ));

        // Check stock
        if (request.getQuantity() > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Only " + product.getStockQuantity()
                            + " items are available"
            );
        }

        // Find or create cart
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());

                    return cartRepository.save(newCart);
                });

        // Check whether product already exists
        Optional<CartItem> existingCartItem =
                cartItemRepository.findByCartAndProduct(cart, product);

        if (existingCartItem.isPresent()) {

            CartItem cartItem = existingCartItem.get();

            int newQuantity =
                    cartItem.getQuantity() + request.getQuantity();

            // Check stock again
            if (newQuantity > product.getStockQuantity()) {
                throw new IllegalArgumentException(
                        "Only " + product.getStockQuantity()
                                + " items are available"
                );
            }

            cartItem.setQuantity(newQuantity);

            cartItemRepository.save(cartItem);

        } else {

            CartItem cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());

            // Save product price at the time of adding
            cartItem.setPrice(product.getPrice());

            cartItemRepository.save(cartItem);

            // Keep both sides of the relationship synchronized
            cart.getCartItems().add(cartItem);
        }

        return convertToCartResponse(cart);
    }

    private CartResponse convertToCartResponse(Cart cart) {

        CartResponse response = new CartResponse();

        response.setCartId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setUsername(cart.getUser().getUsername());

        List<CartItemResponse> items = cart.getCartItems()
                .stream()
                .map(cartItem -> {

                    CartItemResponse itemResponse =
                            new CartItemResponse();

                    itemResponse.setCartItemId(cartItem.getId());

                    itemResponse.setProductId(
                            cartItem.getProduct().getId()
                    );

                    itemResponse.setProductName(
                            cartItem.getProduct().getName()
                    );

                    itemResponse.setImageUrl(
                            cartItem.getProduct().getImageUrl()
                    );

                    itemResponse.setPrice(
                            cartItem.getPrice()
                    );

                    itemResponse.setQuantity(
                            cartItem.getQuantity()
                    );

                    itemResponse.setTotalPrice(
                            cartItem.getPrice()
                                    * cartItem.getQuantity()
                    );

                    return itemResponse;
                })
                .toList();

        response.setItems(items);

        // Total number of products
        int totalItems = items.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();

        // Total cart amount
        double totalAmount = items.stream()
                .mapToDouble(CartItemResponse::getTotalPrice)
                .sum();

        response.setTotalItems(totalItems);
        response.setTotalAmount(totalAmount);

        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());

        return response;
    }

    public CartResponse updateCart(CartRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product with id "
                                        + request.getProductId()
                                        + " not found"
                        ));

        // Check stock
        if (request.getQuantity() > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Only " + product.getStockQuantity()
                            + " items are available"
            );
        }

        // Find user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        // Find product in cart
        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product is not present in cart"
                        )
                );

        // IMPORTANT:
        // Replace existing quantity
        cartItem.setQuantity(request.getQuantity());

        cartItemRepository.save(cartItem);

        return convertToCartResponse(cart);
    }

    public CartResponse removeCartItem(Long cartItemId) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        )
                );

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cart not found"
                        )
                );

        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cart item not found"
                        )
                );

        // Security check:
        // Make sure this cart item belongs to
        // the logged-in user's cart.
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException(
                    "You are not allowed to remove this cart item"
            );
        }

        cartItemRepository.delete(cartItem);

        // Remove from collection also
        cart.getCartItems().remove(cartItem);

        return convertToCartResponse(cart);
    }

    public void clearCart() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        cartItemRepository.deleteAllByCart(cart);
    }
}