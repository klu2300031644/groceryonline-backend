package com.klu.onlinegrocerystore.controller;

import com.klu.onlinegrocerystore.entity.Order;
import com.klu.onlinegrocerystore.entity.User;
import com.klu.onlinegrocerystore.repository.OrderRepository;
import com.klu.onlinegrocerystore.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // Place a new order
    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestParam Long userId,
                                             @RequestParam String shippingAddress) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus("PENDING");
        order.setOrderDate(new Date());
        order.setTotalAmount(0.0); // For real app, calculate from cart items

        orderRepository.save(order);
        return ResponseEntity.ok("Order placed successfully. Order ID: " + order.getId());
    }

    // Get all orders for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Order> orders = orderRepository.findByUser(userOptional.get());
        return ResponseEntity.ok(orders);
    }

    // Get order details by order ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get order status
    @GetMapping("/status/{orderId}")
    public ResponseEntity<String> getOrderStatus(@PathVariable Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.map(order -> ResponseEntity.ok(order.getStatus()))
                            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update order status
    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId,
                                                    @RequestParam String status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Order not found");
        }

        Order order = orderOptional.get();

        if ("SHIPPED".equalsIgnoreCase(order.getStatus()) || 
            "DELIVERED".equalsIgnoreCase(order.getStatus())) {
            return ResponseEntity.badRequest().body("Cannot update status of order that is already " + order.getStatus());
        }

        order.setStatus(status);
        if ("DELIVERED".equalsIgnoreCase(status)) {
            order.setDeliveryDate(new Date());
        }

        orderRepository.save(order);
        return ResponseEntity.ok("Order status updated to: " + status);
    }

    // Update delivery date
    @PutMapping("/update-delivery-date/{orderId}")
    public ResponseEntity<String> updateDeliveryDate(@PathVariable Long orderId,
                                                     @RequestParam Date deliveryDate) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Order not found");
        }

        Order order = orderOptional.get();
        order.setDeliveryDate(deliveryDate);
        orderRepository.save(order);
        return ResponseEntity.ok("Delivery date updated");
    }

    // Get all orders (for admin)
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    // Cancel order
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Order not found");
        }

        Order order = orderOptional.get();
        if ("SHIPPED".equalsIgnoreCase(order.getStatus()) || 
            "DELIVERED".equalsIgnoreCase(order.getStatus())) {
            return ResponseEntity.badRequest().body("Cannot cancel order that is already " + order.getStatus());
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        return ResponseEntity.ok("Order cancelled successfully");
    }
}
