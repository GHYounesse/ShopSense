package com.app.shopsense.doas.repositories.product;

import com.app.shopsense.doas.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}