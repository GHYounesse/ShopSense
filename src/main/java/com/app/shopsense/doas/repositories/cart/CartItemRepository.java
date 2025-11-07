package com.app.shopsense.doas.repositories.cart;

import com.app.shopsense.doas.entities.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
