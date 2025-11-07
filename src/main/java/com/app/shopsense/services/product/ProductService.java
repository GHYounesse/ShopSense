package com.app.shopsense.services.product;

import com.app.shopsense.doas.entities.product.Product;
import com.app.shopsense.doas.repositories.product.ProductRepository;
import com.app.shopsense.dtos.product.ProductDto;
import com.app.shopsense.exceptions.AppException;
import com.app.shopsense.mappers.ProductMapper;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }


    public List<Product> getAllProducts() {
        log.debug("Fetching all products");
        return productRepository.findAll();
    }


    public Product getProductById(Long productId) {
        log.debug("Fetching product with id: {}", productId);
        return productRepository.findById(productId)
                .orElseThrow(() -> new AppException(
                        "Product not found with id: " + productId,
                        HttpStatus.NOT_FOUND
                ));
    }


    @Transactional
    public Product add(ProductDto productDto) {
        log.debug("Creating new product with name: {}", productDto.getName());

        Product product = productMapper.toProduct(productDto);
        Product savedProduct = productRepository.save(product);

        log.info("Product created successfully with id: {}", savedProduct.getId());
        return savedProduct;
    }


    @Transactional
    public Product update(Long id, ProductDto productDto) {
        log.debug("Updating product with id: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        "Product not found with id: " + id,
                        HttpStatus.NOT_FOUND
                ));

        // Update fields
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setImgUrl(productDto.getImgUrl());

        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Product updated successfully with id: {}", updatedProduct.getId());
        return updatedProduct;
    }


    @Transactional
    public void delete(Long id) {
        log.debug("Deleting product with id: {}", id);

        if (!productRepository.existsById(id)) {
            throw new AppException(
                    "Product not found with id: " + id,
                    HttpStatus.NOT_FOUND
            );
        }

        productRepository.deleteById(id);
        log.info("Product deleted successfully with id: {}", id);
    }
}