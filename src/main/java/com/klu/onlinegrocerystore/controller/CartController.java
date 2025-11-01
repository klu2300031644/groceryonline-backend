package com.klu.onlinegrocerystore.controller;

import com.klu.onlinegrocerystore.entity.Cart;
import com.klu.onlinegrocerystore.entity.Product;
import com.klu.onlinegrocerystore.entity.User;
import com.klu.onlinegrocerystore.repository.CartRepository;
import com.klu.onlinegrocerystore.repository.ProductRepository;
import com.klu.onlinegrocerystore.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartController(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Add or update product in cart
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long userId,
                                            @RequestParam Long productId,
                                            @RequestParam int quantity) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (userOptional.isEmpty() || productOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User or Product not found");
        }

        User user = userOptional.get();
        Product product = productOptional.get();

        List<Cart> existingCartItems = cartRepository.findByUserAndProduct(user, product);

        if (!existingCartItems.isEmpty()) {
            Cart cart = existingCartItems.get(0);
            cart.setQuantity(cart.getQuantity() + quantity);
            cartRepository.save(cart);
            return ResponseEntity.ok("Product quantity updated in cart");
        } else {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(quantity);
            cartRepository.save(cart);
            return ResponseEntity.ok("Product added to cart");
        }
    }

    // Get all cart items for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Cart>> getCartItems(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Cart> cartItems = cartRepository.findByUser(userOptional.get());
        return ResponseEntity.ok(cartItems);
    }

    // Update cart item quantity
    @PutMapping("/update/{cartId}")
    public ResponseEntity<String> updateCartItem(@PathVariable Long cartId, @RequestParam int quantity) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Cart item not found");
        }

        Cart cart = cartOptional.get();
        if (quantity <= 0) {
            cartRepository.delete(cart);
            return ResponseEntity.ok("Cart item removed");
        }

        cart.setQuantity(quantity);
        cartRepository.save(cart);
        return ResponseEntity.ok("Cart item updated");
    }

    // Remove a cart item
    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Cart item not found");
        }

        cartRepository.delete(cartOptional.get());
        return ResponseEntity.ok("Product removed from cart");
    }

    // Clear all items from a user's cart
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        List<Cart> cartItems = cartRepository.findByUser(userOptional.get());
        if (cartItems.isEmpty()) {
            return ResponseEntity.ok("Cart is already empty");
        }

        cartRepository.deleteAll(cartItems);
        return ResponseEntity.ok("Cart cleared");
    }
}
