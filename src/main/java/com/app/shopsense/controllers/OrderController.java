package com.app.shopsense.controllers;

import com.app.shopsense.dtos.order.CheckoutRequest;
import com.app.shopsense.dtos.order.CheckoutResponse;
import com.app.shopsense.dtos.order.OrderDetailsDto;
import com.app.shopsense.dtos.order.OrderDto;
import com.app.shopsense.exceptions.AppException;
import com.app.shopsense.services.order.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/orders")

public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getUserOrders(
            @PathVariable Long userId,
            Authentication authentication) {

        log.debug("Fetching orders for user: {}", userId);

        List<OrderDto> orders = orderService.getOrdersByUserId(userId, authentication);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsDto> getOrderById(
            @PathVariable Long orderId,
            Authentication authentication) {

        log.debug("Fetching order details for order: {}", orderId);

        OrderDetailsDto orderDetails = orderService.getOrderDetailsById(orderId, authentication);
        return ResponseEntity.ok(orderDetails);
    }


    @PostMapping("/user/{userId}/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            @PathVariable Long userId,
            @RequestBody(required = false) @Valid CheckoutRequest checkoutRequest,
            Authentication authentication) {

        log.info("Processing checkout for user: {}", userId);

        validateAuthentication(authentication);

        CheckoutResponse response = orderService.processCheckout(userId, checkoutRequest, authentication);

        log.info("Checkout completed successfully for user: {}, order: {}",
                userId, response.getOrderId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


//    @PostMapping("/{orderId}/cancel")
//    public ResponseEntity<OrderDto> cancelOrder(
//            @PathVariable Long orderId,
//            Authentication authentication) {
//
//        log.info("Cancelling order: {}", orderId);
//
//        validateAuthentication(authentication);
//
//        OrderDto cancelledOrder = orderService.cancelOrder(orderId, authentication);
//
//        log.info("Order cancelled successfully: {}", orderId);
//
//        return ResponseEntity.ok(cancelledOrder);
//    }


    @PostMapping("/{orderId}/confirm-payment")
    public ResponseEntity<OrderDto> confirmPayment(
            @PathVariable Long orderId,
            @RequestParam String paymentIntentId,
            Authentication authentication) {

        log.info("Confirming payment for order: {} with payment intent: {}",
                orderId, paymentIntentId);

        validateAuthentication(authentication);

        OrderDto confirmedOrder = orderService.confirmPayment(orderId, paymentIntentId, authentication);

        log.info("Payment confirmed for order: {}", orderId);

        return ResponseEntity.ok(confirmedOrder);
    }


//    @GetMapping("/user/{userId}/statistics")
//    public ResponseEntity<OrderStatisticsDto> getOrderStatistics(
//            @PathVariable Long userId,
//            Authentication authentication) {
//
//        log.debug("Fetching order statistics for user: {}", userId);
//
//        OrderStatisticsDto statistics = orderService.getOrderStatistics(userId, authentication);
//        return ResponseEntity.ok(statistics);
//    }

    // ==================== Private Helper Methods ====================

    /**
     * Validates that user is authenticated
     */
    private void validateAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated access attempt");
            throw new AppException("Authentication required", HttpStatus.UNAUTHORIZED);
        }
    }
}