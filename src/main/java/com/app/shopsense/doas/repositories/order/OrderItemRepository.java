package com.app.shopsense.doas.repositories.order;

import com.app.shopsense.doas.entities.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
