package com.app.shopsense.services.order;

import com.app.shopsense.doas.entities.order.Order;
import com.app.shopsense.doas.entities.order.OrderItem;
import com.app.shopsense.doas.entities.order.OrderStatus;
import com.app.shopsense.doas.entities.product.Product;
import com.app.shopsense.doas.entities.user.User;
import com.app.shopsense.doas.repositories.order.OrderRepository;
import com.app.shopsense.doas.repositories.product.ProductRepository;
import com.app.shopsense.doas.repositories.user.UserRepository;
import com.app.shopsense.dtos.cart.CartDto;
import com.app.shopsense.dtos.cart.CartItemDto;
import com.app.shopsense.dtos.order.CheckoutRequest;
import com.app.shopsense.dtos.order.CheckoutResponse;
import com.app.shopsense.dtos.order.OrderDetailsDto;
import com.app.shopsense.dtos.order.OrderDto;
import com.app.shopsense.exceptions.AppException;
import com.app.shopsense.mappers.OrderMapper;
import com.app.shopsense.services.cart.CartService;
import com.app.shopsense.services.payment.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderMapper orderMapper, CartService cartService, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.cartService = cartService;
        this.paymentService = paymentService;
    }


    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUserId(Long userId, Authentication authentication) {
        log.debug("Fetching orders for user id: {}", userId);

        User user = getUserAndValidateAccess(userId, authentication);

        List<Order> orders = orderRepository.findAllByUserId(userId);
        log.info("Found {} orders for user id: {}", orders.size(), userId);

        return orders.stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public Order createOrderFromCart(CartDto cart, Long userId, Authentication authentication) {
        log.debug("Creating order from cart for user id: {}", userId);

        // Validate user and access
        User user = getUserAndValidateAccess(userId, authentication);

        // Validate cart
        validateCart(cart);

        // Create order
        Order order = buildOrder(user, cart);

        // Create order items
        List<OrderItem> orderItems = buildOrderItems(cart, order);
        order.setOrderItems(orderItems);

        // Calculate and set total
        BigDecimal calculatedTotal = calculateOrderTotal(orderItems);
        order.setTotal(calculatedTotal);

        // Validate cart total matches calculated total
        if (cart.getTotalPrice().compareTo(calculatedTotal) != 0) {
            log.warn("Cart total mismatch. Expected: {}, Actual: {}", calculatedTotal, cart.getTotalPrice());
            throw new AppException("Cart total mismatch. Please refresh your cart.", HttpStatus.BAD_REQUEST);
        }

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with id: {} for user id: {}", savedOrder.getId(), userId);

        return savedOrder;
    }


    public Order getOrderById(Long orderId, Authentication authentication) {
        log.debug("Fetching order with id: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(
                        "Order not found with id: " + orderId,
                        HttpStatus.NOT_FOUND
                ));

        // Validate user has access to this order
        if (authentication == null || !order.getUser().getEmail().equals(authentication.getName())) {
            log.warn("Unauthorized access attempt to order id: {} by user: {}",
                    orderId, authentication != null ? authentication.getName() : "anonymous");
            throw new AppException("Access denied.", HttpStatus.FORBIDDEN);
        }

        return order;
    }


    @Transactional
    public CheckoutResponse processCheckout(
            Long userId,
            CheckoutRequest request,
            Authentication authentication) {

        // Get and validate cart
        CartDto cart = cartService.getCartByUserId(userId);
        validateCart(cart);

        // Create payment intent
        String currency = request != null ? request.getCurrency() : "usd";
        PaymentIntent paymentIntent;
        try {

            paymentIntent = paymentService.createStripePaymentIntent(
                    cart.getTotalPrice(),
                    currency,
                    buildMetadata(userId, request)
            );
        } catch (
                StripeException e) {
            log.error("Stripe error while creating payment intent: {}", e.getMessage(), e);
            throw new AppException(
                    "Failed to create payment: " + e.getUserMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Create order
        Order order = createOrderFromCart(cart, userId, authentication);

        // Add additional details if provided
        if (request != null) {
            order.setShippingAddress(request.getShippingAddress());
            order.setBillingAddress(request.getBillingAddress());
            order.setNotes(request.getNotes());
        }

        order.setPaymentIntentId(paymentIntent.getId());
        order = orderRepository.save(order);

        // Clear cart after successful order creation
        cartService.clearCart(userId);

        return CheckoutResponse.builder()
                .clientSecret(paymentIntent.getClientSecret())
                .orderId(order.getId())
                .totalAmount(cart.getTotalPrice())
                .currency(currency)
                .paymentIntentId(paymentIntent.getId())
                .status("pending")
                .build();
    }

    @Transactional(readOnly = true)
    public OrderDetailsDto getOrderDetailsById(Long orderId, Authentication authentication) {
        Order order = getOrderById(orderId, authentication);
        return orderMapper.toOrderDetailsDto(order);
    }

    @Transactional(readOnly = true)
    public OrderDto confirmPayment(Long orderId, String paymentIntentId, Authentication authentication) {
        Order order = getOrderById(orderId, authentication);

        // Verify payment with Stripe
        PaymentIntent paymentIntent = paymentService.getPaymentIntent(paymentIntentId);

        if (!"succeeded".equals(paymentIntent.getStatus())) {
            throw new AppException("Payment not successful", HttpStatus.BAD_REQUEST);
        }

        order.setStatus(OrderStatus.CONFIRMED);
        order.setPaymentConfirmedAt(LocalDateTime.now());
        order = orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

//    @Transactional
//    public void cancelOrder(Long orderId, Authentication authentication) {
//        log.debug("Cancelling order with id: {}", orderId);
//
//        Order order = getOrderById(orderId, authentication);
//
//        // Add business logic for cancellation (e.g., check if order can be cancelled)
//        // order.setStatus(OrderStatus.CANCELLED);
//        // orderRepository.save(order);
//
//        log.info("Order cancelled successfully with id: {}", orderId);
//    }

    // ==================== Private Helper Methods ====================

    /**
     * Retrieves user and validates access
     */
    private User getUserAndValidateAccess(Long userId, Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(
                        "User not found with id: " + userId,
                        HttpStatus.NOT_FOUND
                ));

        if (authentication == null || !user.getEmail().equals(authentication.getName())) {
            log.warn("Unauthorized access attempt for user id: {} by: {}",
                    userId, authentication != null ? authentication.getName() : "anonymous");
            throw new AppException("Access denied.", HttpStatus.FORBIDDEN);
        }

        return user;
    }

    /**
     * Validates cart data
     */
    private void validateCart(CartDto cart) {
        if (cart == null) {
            throw new AppException("Cart cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new AppException("Cart is empty", HttpStatus.BAD_REQUEST);
        }

        if (cart.getTotalPrice() == null || cart.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException("Invalid cart total", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Builds the order entity
     */
    private Order buildOrder(User user, CartDto cart) {
        Order order = new Order();
        order.setUser(user);
        order.setDateCreated(LocalDateTime.now());
        // order.setStatus(OrderStatus.PENDING); // If you have order status
        return order;
    }

    /**
     * Builds order items from cart items
     */
    private List<OrderItem> buildOrderItems(CartDto cart, Order order) {
        return cart.getCartItems().stream()
                .map(cartItem -> createOrderItem(cartItem, order))
                .collect(Collectors.toList());
    }

    /**
     * Creates a single order item from cart item
     */
    private OrderItem createOrderItem(CartItemDto cartItem, Order order) {
        if (cartItem.getQuantity() <= 0) {
            throw new AppException("Invalid quantity for product id: " + cartItem.getProductId(),
                    HttpStatus.BAD_REQUEST);
        }

        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new AppException(
                        "Product not found with id: " + cartItem.getProductId(),
                        HttpStatus.NOT_FOUND
                ));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(product.getPrice()); // Store price at time of order

        return orderItem;
    }

    /**
     * Calculates total price from order items
     */
    private BigDecimal calculateOrderTotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private Map<String, String> buildMetadata(Long userId, CheckoutRequest request) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("userId", userId.toString());

        if (request != null && request.getMetadata() != null) {
            metadata.putAll(request.getMetadata());
        }

        return metadata;
    }
}