package com.klu.onlinegrocerystore.service;

import com.klu.onlinegrocerystore.entity.Delivery;
import com.klu.onlinegrocerystore.entity.Order;
import com.klu.onlinegrocerystore.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery scheduleDelivery(Order order, String deliveryAddress, Date estimatedArrival) {
        Delivery delivery = new Delivery(order, deliveryAddress, estimatedArrival);
        return deliveryRepository.save(delivery);
    }

    public Delivery updateDeliveryStatus(Long deliveryId, String status) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            delivery.setStatus(status);
            return deliveryRepository.save(delivery);
        }
        return null;
    }

    public Delivery updateActualArrival(Long deliveryId, Date actualArrival) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            delivery.setActualArrival(actualArrival);
            delivery.setStatus("DELIVERED");
            return deliveryRepository.save(delivery);
        }
        return null;
    }

    public Delivery assignDeliveryPerson(Long deliveryId, String deliveryPersonName, String deliveryPersonContact) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            delivery.setDeliveryPersonName(deliveryPersonName);
            delivery.setDeliveryPersonContact(deliveryPersonContact);
            delivery.setStatus("ASSIGNED");
            return deliveryRepository.save(delivery);
        }
        return null;
    }

    public Delivery updateTrackingNumber(Long deliveryId, String trackingNumber) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            delivery.setTrackingNumber(trackingNumber);
            return deliveryRepository.save(delivery);
        }
        return null;
    }

    public Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId).orElse(null);
    }

    public List<Delivery> getDeliveriesByStatus(String status) {
        return deliveryRepository.findByStatus(status);
    }

    public List<Delivery> getDeliveriesByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    public boolean cancelDelivery(Long deliveryId) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            if (!"DELIVERED".equals(delivery.getStatus())) {
                delivery.setStatus("CANCELLED");
                deliveryRepository.save(delivery);
                return true;
            }
        }
        return false;
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }
}