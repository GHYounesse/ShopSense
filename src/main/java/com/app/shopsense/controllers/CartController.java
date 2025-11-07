package com.app.shopsense.controllers;

import com.app.shopsense.dtos.cart.AddToCartRequest;
import com.app.shopsense.dtos.cart.CartDto;
import com.app.shopsense.dtos.cart.UpdateCartItemRequest;
import com.app.shopsense.services.cart.CartService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@securityService.isUserOrAdmin(#userId)")
    public ResponseEntity<CartDto> getCartByUserId(
            @PathVariable Long userId) {

        log.info("GET request to fetch cart for userId: {}", userId);
        CartDto cartDto = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/{userId}/count")
    @PreAuthorize("@securityService.isUserOrAdmin(#userId)")
    public ResponseEntity<Integer> getCartItemCount(
            @PathVariable Long userId) {

        log.info("GET request to fetch cart item count for userId: {}", userId);
        int count = cartService.getNumberOfItemsInCart(userId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/{userId}/items")
    @PreAuthorize("@securityService.isUserOrAdmin(#userId)")
    public ResponseEntity<CartDto> addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody AddToCartRequest request) {

        log.info("POST request to add item to cart: userId={}, productId={}, quantity={}",
                userId, request.getProductId(), request.getQuantity());

        CartDto cartDto = cartService.addItemToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    @PutMapping("/{userId}/items/{productId}")
    @PreAuthorize("@securityService.isUserOrAdmin(#userId)")
    public ResponseEntity<CartDto> updateCartItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        log.info("PUT request to update cart item: userId={}, productId={}, newQuantity={}",
                userId, productId, request.getQuantity());

        CartDto cartDto = cartService.updateCartItemQuantity(userId, productId, request.getQuantity());
        return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    @PreAuthorize("@securityService.isUserOrAdmin(#userId)")
    public ResponseEntity<CartDto> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        log.info("DELETE request to remove item from cart: userId={}, productId={}", userId, productId);
        CartDto cartDto = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@securityService.isUserOrAdmin(#userId)")
    public ResponseEntity<Void> clearCart(
            @PathVariable Long userId) {

        log.info("DELETE request to clear cart for userId: {}", userId);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    // Alternative endpoint using query parameters (keep for backward compatibility if needed)
//    @PostMapping("/{userId}/items/simple")
//    @PreAuthorize("@securityService.isUserOrAdmin(#userId)")
//    @Deprecated
//    public ResponseEntity<CartDto> addItemToCartSimple(
//            @PathVariable Long userId,
//            @RequestParam Long productId,
//            @RequestParam @Min(1) @Max(999) int quantity) {
//
//        log.info("POST request (legacy) to add item to cart: userId={}, productId={}, quantity={}",
//                userId, productId, quantity);
//
//        CartDto cartDto = cartService.addItemToCart(userId, productId, quantity);
//        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
//    }
}
