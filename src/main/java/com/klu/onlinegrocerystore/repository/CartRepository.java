package com.klu.onlinegrocerystore.repository;

import com.klu.onlinegrocerystore.entity.Cart;
import com.klu.onlinegrocerystore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    List<Cart> findByUserAndProduct(User user, com.klu.onlinegrocerystore.entity.Product product);
}