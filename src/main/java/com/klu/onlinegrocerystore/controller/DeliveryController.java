package com.klu.onlinegrocerystore.controller;

import com.klu.onlinegrocerystore.entity.Delivery;
import com.klu.onlinegrocerystore.entity.Order;
import com.klu.onlinegrocerystore.service.DeliveryService;
import com.klu.onlinegrocerystore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@CrossOrigin(origins = "http://localhost:3000")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final OrderService orderService;

    public DeliveryController(DeliveryService deliveryService, OrderService orderService) {
        this.deliveryService = deliveryService;
        this.orderService = orderService;
    }

    // Get all deliveries
    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    // Get delivery by ID
    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long id) {
        Delivery delivery = deliveryService.getDeliveryById(id);
        if (delivery == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(delivery);
    }

    // Schedule a delivery for an order
    @PostMapping("/schedule")
    public ResponseEntity<Delivery> scheduleDelivery(
            @RequestParam Long orderId,
            @RequestParam String address) {

        Order order = orderService.getOrderById(orderId);
        if (order == null) return ResponseEntity.badRequest().build();

        Delivery delivery = deliveryService.scheduleDelivery(order, address, new Date());
        return ResponseEntity.ok(delivery);
    }

    // Update delivery status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Delivery> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Delivery updated = deliveryService.updateDeliveryStatus(id, status);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // Cancel delivery
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelDelivery(@PathVariable Long id) {
        boolean cancelled = deliveryService.cancelDelivery(id);
        if (!cancelled) return ResponseEntity.badRequest().body("Cannot cancel delivery");
        return ResponseEntity.ok("Delivery cancelled successfully");
    }
}
