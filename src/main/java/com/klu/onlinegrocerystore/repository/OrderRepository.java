package com.klu.onlinegrocerystore.repository;

import com.klu.onlinegrocerystore.entity.Order;
import com.klu.onlinegrocerystore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByStatus(String status);
}