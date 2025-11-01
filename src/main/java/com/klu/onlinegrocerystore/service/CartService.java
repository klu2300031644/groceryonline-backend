package com.klu.onlinegrocerystore.service;

import com.klu.onlinegrocerystore.entity.Cart;
import com.klu.onlinegrocerystore.entity.Product;
import com.klu.onlinegrocerystore.entity.User;
import com.klu.onlinegrocerystore.repository.CartRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public Cart addToCart(User user, Product product, int quantity) {
        Cart cartItem = new Cart(user, product, quantity);
        return cartRepository.save(cartItem);
    }

    public void removeFromCart(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public void updateCartItemQuantity(Long cartItemId, int quantity) {
        Cart cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItem.setQuantity(quantity);
        cartRepository.save(cartItem);
    }

    public void clearUserCart(User user) {
        List<Cart> userCartItems = cartRepository.findByUser(user);
        cartRepository.deleteAll(userCartItems);
    }
}