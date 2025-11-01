package com.klu.onlinegrocerystore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnoreProperties({"delivery", "user"}) // Ignore back references to avoid infinite loops
    private Order order;

    @Column(name = "delivery_person_name")
    private String deliveryPersonName;

    @Column(name = "delivery_person_contact")
    private String deliveryPersonContact;

    @Column(name = "estimated_arrival")
    @Temporal(TemporalType.TIMESTAMP)
    private Date estimatedArrival;

    @Column(name = "actual_arrival")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualArrival;

    @Column(nullable = false)
    private String status;

    @Column(name = "tracking_number", unique = true)
    private String trackingNumber;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    public Delivery() {}

    public Delivery(Order order, String deliveryAddress, Date estimatedArrival) {
        this.order = order;
        this.deliveryAddress = deliveryAddress;
        this.estimatedArrival = estimatedArrival;
        this.status = "SCHEDULED";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public String getDeliveryPersonName() { return deliveryPersonName; }
    public void setDeliveryPersonName(String deliveryPersonName) { this.deliveryPersonName = deliveryPersonName; }

    public String getDeliveryPersonContact() { return deliveryPersonContact; }
    public void setDeliveryPersonContact(String deliveryPersonContact) { this.deliveryPersonContact = deliveryPersonContact; }

    public Date getEstimatedArrival() { return estimatedArrival; }
    public void setEstimatedArrival(Date estimatedArrival) { this.estimatedArrival = estimatedArrival; }

    public Date getActualArrival() { return actualArrival; }
    public void setActualArrival(Date actualArrival) { this.actualArrival = actualArrival; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
}
